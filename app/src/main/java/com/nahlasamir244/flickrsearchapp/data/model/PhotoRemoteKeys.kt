package com.nahlasamir244.flickrsearchapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_remote_keys_table")
data class PhotoRemoteKeys(
    @PrimaryKey
    val photoId:String,
    val prevKey: Int?,
    val nextKey: Int?
)