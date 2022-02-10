package com.nahlasamir244.flickrsearchapp.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.nahlasamir244.flickrsearchapp.application.FlickrSearchApp
import com.nahlasamir244.flickrsearchapp.data.api.PhotoApiService
import com.nahlasamir244.flickrsearchapp.data.db.FlickrDatabaseTransactionProvider
import com.nahlasamir244.flickrsearchapp.data.db.PhotoDao
import com.nahlasamir244.flickrsearchapp.data.db.PhotoRemoteKeysDao
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import com.nahlasamir244.flickrsearchapp.data.model.PhotoRemoteKeys
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator(
    private val query: String
) : RemoteMediator<Int, Photo>() {
    var photoApiService: PhotoApiService = getService()
    var photoDao: PhotoDao = getDao()
    var remoteKeysDao: PhotoRemoteKeysDao = getRemoteKeysDao()
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
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: PAGE_STARTING_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(
                        endOfPaginationReached =
                        remoteKeys != null
                    )
                prevKey
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
        val apiQuery = query
        try {
            val apiResponse =
                photoApiService.getPhotoSearchList(apiQuery, page, state.config.pageSize)
            val photos = apiResponse.body()?.photoSearchResponseMeta?.photoList
            val endOfPaginationReached = photos?.isEmpty() ?: true
            flickrDatabaseTransactionProvider.runAsTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.deleteAll()
                    photoDao.deleteAll()
                }
                val prevKey = if (page == PAGE_STARTING_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = photos?.map {
                    PhotoRemoteKeys(photoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                if (keys != null) {
                    remoteKeysDao.insertAll(keys)
                }
                if (photos != null) {
                    photoDao.insertAll(photos)
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
                remoteKeysDao.getRemoteKeysByPhotoId(photo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Photo>): PhotoRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { photo ->
                remoteKeysDao.getRemoteKeysByPhotoId(photo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Photo>
    ): PhotoRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { photoId ->
                remoteKeysDao.getRemoteKeysByPhotoId(photoId)
            }
        }
    }


    private fun getService() =
        EntryPoints.get(FlickrSearchApp.getAppContext(), RemoteMediatorEntryPoint::class.java)
            .getPhotoApiService()

    private fun getDao() =
        EntryPoints.get(FlickrSearchApp.getAppContext(), RemoteMediatorEntryPoint::class.java)
            .getPhotoDao()

    @JvmName("getRemoteKeysDao1")
    private fun getRemoteKeysDao() =
        EntryPoints.get(FlickrSearchApp.getAppContext(), RemoteMediatorEntryPoint::class.java)
            .getPhotoRemoteKeysDao()

    private fun getTransactionProvider(): FlickrDatabaseTransactionProvider =
        EntryPoints.get(FlickrSearchApp.getAppContext(), RemoteMediatorEntryPoint::class.java)
            .getFlickrDatabaseTransactionProvider()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RemoteMediatorEntryPoint {
        fun getPhotoApiService(): PhotoApiService
        fun getPhotoDao(): PhotoDao
        fun getPhotoRemoteKeysDao(): PhotoRemoteKeysDao
        fun getFlickrDatabaseTransactionProvider(): FlickrDatabaseTransactionProvider
    }
}