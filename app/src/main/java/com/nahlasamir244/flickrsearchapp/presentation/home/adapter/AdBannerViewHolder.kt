package com.nahlasamir244.flickrsearchapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nahlasamir244.flickrsearchapp.R

class AdBannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.textView_ad)

    fun bind(text: String) {
        title.text =text
    }

    companion object {
        fun create(parent: ViewGroup): AdBannerViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ad_banner, parent, false)
            return AdBannerViewHolder(view)
        }
    }
}