package com.nahlasamir244.flickrsearchapp.domain

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide

typealias LoadPhoto = LoadPhotoUseCase
class LoadPhotoUseCase {
    operator fun invoke(imageView: ImageView,url: String, placeholder: Drawable) =
        Glide.with(imageView).load(url).placeholder(placeholder).into(imageView)
}