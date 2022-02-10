package com.nahlasamir244.flickrsearchapp.presentation.home.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nahlasamir244.flickrsearchapp.R
import com.nahlasamir244.flickrsearchapp.data.model.Photo

class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.textView_photoTitle)
    private var photo:Photo? = null

    init {
        view.setOnClickListener {
        }
    }

    fun bind(photo: Photo?) {
        if (photo == null) {
            val resources = itemView.resources
            title.text = resources.getString(R.string.photo_title_example)
        } else {
            showPhotoData(photo)
        }
    }

    private fun showPhotoData(photo: Photo?) {
        this.photo = photo
        title.text = photo?.title

    }

    companion object {
        fun create(parent: ViewGroup): PhotoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false)
            return PhotoViewHolder(view)
        }
    }
}
