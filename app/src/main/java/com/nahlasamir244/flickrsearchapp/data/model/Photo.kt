package com.nahlasamir244.flickrsearchapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "photo_table")
data class Photo(

    @PrimaryKey @SerializedName("id") var id: String,
    @SerializedName("owner") var ownerId: String?,
    @SerializedName("secret") var secret: String?,
    @SerializedName("server") var serverId: String?,
    @SerializedName("farm") var farm: Int?,
    @SerializedName("title") var title: String?,
    @SerializedName("ispublic") var isPublic: Int?,
    @SerializedName("isfriend") var isFriend: Int?,
    @SerializedName("isfamily") var isFamily: Int?,
    var index:Int

)