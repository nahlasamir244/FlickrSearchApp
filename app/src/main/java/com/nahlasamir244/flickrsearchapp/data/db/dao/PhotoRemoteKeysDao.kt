package com.nahlasamir244.flickrsearchapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nahlasamir244.flickrsearchapp.data.model.PhotoRemoteKeys

@Dao
interface PhotoRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photosRemoteKeys: List<PhotoRemoteKeys>)

    @Query("SELECT * FROM photo_remote_keys_table WHERE photoId = :photoId")
    suspend fun getRemoteKeysByPhotoId(photoId: String): PhotoRemoteKeys?

    @Query("DELETE FROM photo_remote_keys_table")
    suspend fun deleteAll()
}