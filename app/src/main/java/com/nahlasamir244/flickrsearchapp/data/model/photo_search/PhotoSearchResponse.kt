package com.nahlasamir244.flickrsearchapp.data.model.photo_search

import com.google.gson.annotations.SerializedName


data class PhotoSearchResponse(

    @SerializedName("photos") var photoSearchResponseMeta: PhotoSearchResponseMeta?,
    @SerializedName("stat") var state: String?

)