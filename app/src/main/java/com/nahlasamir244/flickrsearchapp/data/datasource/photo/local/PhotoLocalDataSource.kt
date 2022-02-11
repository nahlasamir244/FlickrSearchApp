package com.nahlasamir244.flickrsearchapp.data.datasource.photo.local

import androidx.paging.PagingSource
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import com.nahlasamir244.flickrsearchapp.data.model.PhotoRemoteKeys

interface PhotoLocalDataSource {
    suspend fun savePhotos(photos: List<Photo>)
    fun findPhotosByTitle(queryString: String): PagingSource<Int, Photo>
    suspend fun clearPhotos()
    suspend fun saveRemoteKeys(photosRemoteKeys: List<PhotoRemoteKeys>)
    suspend fun getRemoteKeysForPhoto(photoId: String): PhotoRemoteKeys?
    suspend fun clearRemoteKeys()
}