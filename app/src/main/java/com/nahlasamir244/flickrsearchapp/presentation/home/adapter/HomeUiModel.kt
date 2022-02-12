package com.nahlasamir244.flickrsearchapp.presentation.home.adapter

import com.nahlasamir244.flickrsearchapp.data.model.Ad
import com.nahlasamir244.flickrsearchapp.data.model.Photo

sealed class HomeUiModel {
    data class PhotoItem(val photo: Photo) : HomeUiModel()
    data class AdBannerItem(val ad: Ad) : HomeUiModel()
}