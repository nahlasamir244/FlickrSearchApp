package com.nahlasamir244.flickrsearchapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import com.nahlasamir244.flickrsearchapp.data.model.PhotoRemoteKeys

@Database(
    entities = [Photo::class, PhotoRemoteKeys::class],
    version = 1
)
abstract class FlickrSearchAppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao
    abstract fun photoRemoteKeysDao(): PhotoRemoteKeysDao

}