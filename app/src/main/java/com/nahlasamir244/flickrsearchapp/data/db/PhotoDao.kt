package com.nahlasamir244.flickrsearchapp.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nahlasamir244.flickrsearchapp.data.model.Photo

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<Photo>)

    @Query("SELECT * FROM  photo_table WHERE title LIKE :queryString ORDER BY title ASC")
    fun getPhotosByTitle(queryString: String): PagingSource<Int, Photo>

    @Query("DELETE FROM photo_table")
    suspend fun deleteAll()
}