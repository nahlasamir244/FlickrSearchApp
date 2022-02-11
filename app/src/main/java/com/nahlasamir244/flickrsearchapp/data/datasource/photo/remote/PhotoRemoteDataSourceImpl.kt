package com.nahlasamir244.flickrsearchapp.data.datasource.photo.remote

import com.nahlasamir244.flickrsearchapp.data.api.PhotoApiService
import com.nahlasamir244.flickrsearchapp.data.model.photo_search.PhotoSearchResponse
import retrofit2.Response
import javax.inject.Inject

class PhotoRemoteDataSourceImpl @Inject constructor(
    private val photoApiService: PhotoApiService
) : PhotoRemoteDataSource {
    override suspend fun getPhotoSearchList(
        searchQuery: String,
        pageNumber: Int,
        numberOfPhotosPerPage: Int
    ): Response<PhotoSearchResponse> = photoApiService.getPhotoSearchList(
        searchQuery, pageNumber, numberOfPhotosPerPage
    )

}