package com.nahlasamir244.flickrsearchapp.data.model

import com.google.gson.annotations.SerializedName


data class Photo(

    @SerializedName("id") var id: String? ,
    @SerializedName("owner") var ownerId: String? ,
    @SerializedName("secret") var secret: String? ,
    @SerializedName("server") var serverId: String? ,
    @SerializedName("farm") var farm: Int? ,
    @SerializedName("title") var title: String? ,
    @SerializedName("ispublic") var isPublic: Int? ,
    @SerializedName("isfriend") var isFriend: Int? ,
    @SerializedName("isfamily") var isFamily: Int?

)