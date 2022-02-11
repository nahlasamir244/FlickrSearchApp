package com.nahlasamir244.flickrsearchapp.data.api

import com.nahlasamir244.flickrsearchapp.data.model.photo_search.PhotoSearchResponse
import com.nahlasamir244.flickrsearchapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApiService {
    @GET(".")
    suspend fun getPhotoSearchList(
        @Query("text") searchQuery: String,
        @Query("page") pageNumber: Int,
        @Query("per_page") numberOfPhotosPerPage: Int,
        @Query("method") method: String = "flickr.photos.search",
        @Query("nojsoncallback") noJson: Int = 50,
        @Query("format") format: String = Constants.RESPONSE_FORMAT,
        @Query("api_key") apiKey: String = Constants.FLICKR_API_KEY,
    ): Response<PhotoSearchResponse>
}