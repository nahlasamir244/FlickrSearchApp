package com.nahlasamir244.flickrsearchapp.data.datasource.photo.local

import androidx.paging.PagingSource
import com.nahlasamir244.flickrsearchapp.data.db.dao.PhotoDao
import com.nahlasamir244.flickrsearchapp.data.db.dao.PhotoRemoteKeysDao
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import com.nahlasamir244.flickrsearchapp.data.model.PhotoRemoteKeys
import javax.inject.Inject

class PhotoLocalDataSourceImpl @Inject constructor(
    private val photoDao: PhotoDao,
    private val photoRemoteKeysDao: PhotoRemoteKeysDao
) : PhotoLocalDataSource {
    override suspend fun savePhotos(photos: List<Photo>) {
        photoDao.insertAll(photos)
    }

    override fun findPhotosByTitle(queryString: String): PagingSource<Int, Photo> =
        photoDao.getPhotosByTitle(queryString)

    override suspend fun clearPhotos() {
        photoDao.deleteAll()
    }

    override suspend fun saveRemoteKeys(photosRemoteKeys: List<PhotoRemoteKeys>) {
        photoRemoteKeysDao.insertAll(photosRemoteKeys)
    }

    override suspend fun getRemoteKeysForPhoto(photoId: String): PhotoRemoteKeys? =
        photoRemoteKeysDao.getRemoteKeysByPhotoId(photoId)

    override suspend fun clearRemoteKeys() {
        photoRemoteKeysDao.deleteAll()
    }

}