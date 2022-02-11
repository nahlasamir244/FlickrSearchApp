package com.nahlasamir244.flickrsearchapp.data.datasource.photo.remote

import com.nahlasamir244.flickrsearchapp.data.model.photo_search.PhotoSearchResponse
import retrofit2.Response

interface PhotoRemoteDataSource {
    suspend fun getPhotoSearchList(
        searchQuery: String,
        pageNumber: Int,
        numberOfPhotosPerPage: Int
    ): Response<PhotoSearchResponse>
}