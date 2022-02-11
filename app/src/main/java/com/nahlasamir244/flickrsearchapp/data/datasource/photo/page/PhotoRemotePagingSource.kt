package com.nahlasamir244.flickrsearchapp.data.datasource.photo.page

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nahlasamir244.flickrsearchapp.application.FlickrSearchApp
import com.nahlasamir244.flickrsearchapp.data.datasource.photo.remote.PhotoRemoteDataSource
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException
import java.io.IOException

class PhotoRemotePagingSource(
    private val searchQuery: String
) : PagingSource<Int, Photo>() {
    var photoRemoteDataSource: PhotoRemoteDataSource = getRemoteDataSource()

    companion object {
        private const val PAGE_STARTING_INDEX = 1
        private const val PAGE_SIZE = 30
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val currentPosition = params.key ?: PAGE_STARTING_INDEX
        return try {
            val response = photoRemoteDataSource.getPhotoSearchList(
                searchQuery,
                currentPosition, params.loadSize
            )
            val photoList = response.body()?.photoSearchResponseMeta?.photoList
            val photos = photoList?.map { photo ->
                photo.index = photoList.indexOf(photo)
                photo
            }
            val nextKey = if (photos?.isNullOrEmpty() == true) {
                null
            } else {
                currentPosition + (params.loadSize / PAGE_SIZE)
            }
            LoadResult.Page(
                data = photos,
                prevKey = if (currentPosition == PAGE_STARTING_INDEX) null else currentPosition - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    private fun getRemoteDataSource() =
        EntryPoints.get(FlickrSearchApp.getAppContext(), PagingSourceEntryPoint::class.java)
            .getPhotoRemoteDataSource()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface PagingSourceEntryPoint {
        fun getPhotoRemoteDataSource(): PhotoRemoteDataSource

    }
}