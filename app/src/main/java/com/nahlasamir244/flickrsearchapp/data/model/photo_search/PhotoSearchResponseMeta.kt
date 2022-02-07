package com.nahlasamir244.flickrsearchapp.data.model.photo_search

import com.google.gson.annotations.SerializedName
import com.nahlasamir244.flickrsearchapp.data.model.Photo


data class PhotoSearchResponseMeta(

    @SerializedName("page") var pageNumber: Int?,
    @SerializedName("pages") var totalNumberOfPages: Int?,
    @SerializedName("perpage") var numberOfPhotosPerPage: Int?,
    @SerializedName("total") var totalPhotosCount: Int?,
    @SerializedName("photo") var photoList: ArrayList<Photo>

)