package com.nahlasamir244.flickrsearchapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahlasamir244.flickrsearchapp.R
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import com.nahlasamir244.flickrsearchapp.databinding.ItemPhotoBinding
import com.nahlasamir244.flickrsearchapp.domain.GetUrl
import com.nahlasamir244.flickrsearchapp.domain.LoadPhoto

class PhotoViewHolder(
    private val binding: ItemPhotoBinding,
    photoAdapterHandler: PhotoAdapterHandler
) : RecyclerView.ViewHolder(binding.root) {
    private var photo: Photo? = null

    init {
        binding.root.setOnClickListener {
            photoAdapterHandler.onPhotoClicked(photo)
        }
    }

    fun bind(photo: Photo?) {
        this.photo = photo
        val getUrl = GetUrl()
        val loadPhoto = LoadPhoto()
        binding.apply {
            photo?.let { getUrl(it) }?.let {
                loadPhoto(
                    imageViewPhotoImage,
                    it, R.drawable.ic_image_default_grey
                )
            }
            textViewPhotoTitle.text = photo?.title
        }

    }

    companion object {
        fun create(parent: ViewGroup, photoAdapterHandler: PhotoAdapterHandler): PhotoViewHolder {
            val binding = ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return PhotoViewHolder(binding, photoAdapterHandler)
        }
    }
}
