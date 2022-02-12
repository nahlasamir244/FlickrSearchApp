package com.nahlasamir244.flickrsearchapp.presentation.photo_preview

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import com.nahlasamir244.flickrsearchapp.domain.GetUrl

class PhotoPreviewViewModel
@ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val photo: Photo? = savedStateHandle.get<Photo>("photo")
    private val getUrl = GetUrl()
    var photoUrl: String? = photo?.let { getUrl(it) }
}