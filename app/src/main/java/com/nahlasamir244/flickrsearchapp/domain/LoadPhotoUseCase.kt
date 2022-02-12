package com.nahlasamir244.flickrsearchapp.domain

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.nahlasamir244.flickrsearchapp.application.FlickrSearchApp

typealias LoadPhoto = LoadPhotoUseCase

class LoadPhotoUseCase {
    operator fun invoke(imageView: ImageView, url: String, placeholderResourceId: Int) =
        Glide.with(imageView).load(url)
            .placeholder(
                ContextCompat.getDrawable(
                    FlickrSearchApp.getAppContext(),
                    placeholderResourceId
                )
            ).into(imageView)
}