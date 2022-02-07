package com.nahlasamir244.flickrsearchapp.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String, placeholder: Drawable) =
    Glide.with(this).load(url).placeholder(placeholder).into(this)

fun ImageView.loadImageRound(url: String, placeholder: Drawable) =
    Glide.with(this).load(url)
        .placeholder(placeholder)
        .circleCrop()
        .into(this)
