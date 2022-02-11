package com.nahlasamir244.flickrsearchapp.data.datasource.photo.page

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.nahlasamir244.flickrsearchapp.application.FlickrSearchApp
import com.nahlasamir244.flickrsearchapp.data.datasource.photo.local.PhotoLocalDataSource
import com.nahlasamir244.flickrsearchapp.data.datasource.photo.remote.PhotoRemoteDataSource
import com.nahlasamir244.flickrsearchapp.data.db.database.FlickrDatabaseTransactionProvider
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import com.nahlasamir244.flickrsearchapp.data.model.PhotoRemoteKeys
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PhotoPagingSourceMediator(
    private val searchQuery: String
) : RemoteMediator<Int, Photo>() {
    var photoRemoteDataSource: PhotoRemoteDataSource = getRemoteDataSource()
    var photoLocalDataSource: PhotoLocalDataSource = getLocalDataSource()
    var flickrDatabaseTransactionProvider: FlickrDatabaseTransactionProvider =
        getTransactionProvider()

    companion object {
        private const val PAGE_STARTING_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Photo>):
            MediatorResult {
        val pageNumber = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: PAGE_STARTING_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val previousKey = remoteKeys?.previousKey
                    ?: return MediatorResult.Success(
                        endOfPaginationReached =
                        remoteKeys != null
                    )
                previousKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(
                        endOfPaginationReached =
                        remoteKeys != null
                    )
                nextKey
            }
        }
        val searchApiQuery = searchQuery
        try {
            val searchApiResponse =
                photoRemoteDataSource.getPhotoSearchList(
                    searchApiQuery,
                    pageNumber,
                    state.config.pageSize
                )
            val photos = searchApiResponse.body()?.photoSearchResponseMeta?.photoList
            val endOfPaginationReached = photos?.isEmpty() ?: true
            flickrDatabaseTransactionProvider.runAsTransaction {
                if (loadType == LoadType.REFRESH) {
                    photoLocalDataSource.clearRemoteKeys()
                    photoLocalDataSource.clearPhotos()
                }
                val prevKey = if (pageNumber == PAGE_STARTING_INDEX) null else pageNumber - 1
                val nextKey = if (endOfPaginationReached) null else pageNumber + 1
                val keys = photos?.map {
                    PhotoRemoteKeys(photoId = it.id, previousKey = prevKey, nextKey = nextKey)
                }
                if (keys != null) {
                    photoLocalDataSource.saveRemoteKeys(keys)
                }
                if (photos != null) {
                    photoLocalDataSource.savePhotos(photos)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Photo>): PhotoRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { photo ->
                photoLocalDataSource.getRemoteKeysForPhoto(photo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Photo>): PhotoRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { photo ->
                photoLocalDataSource.getRemoteKeysForPhoto(photo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Photo>
    ): PhotoRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { photoId ->
                photoLocalDataSource.getRemoteKeysForPhoto(photoId)
            }
        }
    }

    private fun getRemoteDataSource(): PhotoRemoteDataSource =
        EntryPoints.get(FlickrSearchApp.getAppContext(), RemoteMediatorEntryPoint::class.java)
            .getPhotoRemoteDataSource()

    private fun getLocalDataSource(): PhotoLocalDataSource =
        EntryPoints.get(FlickrSearchApp.getAppContext(), RemoteMediatorEntryPoint::class.java)
            .getPhotoLocalDataSource()

    private fun getTransactionProvider(): FlickrDatabaseTransactionProvider =
        EntryPoints.get(FlickrSearchApp.getAppContext(), RemoteMediatorEntryPoint::class.java)
            .getFlickrDatabaseTransactionProvider()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RemoteMediatorEntryPoint {
        fun getPhotoRemoteDataSource(): PhotoRemoteDataSource
        fun getPhotoLocalDataSource(): PhotoLocalDataSource
        fun getFlickrDatabaseTransactionProvider(): FlickrDatabaseTransactionProvider
    }
}