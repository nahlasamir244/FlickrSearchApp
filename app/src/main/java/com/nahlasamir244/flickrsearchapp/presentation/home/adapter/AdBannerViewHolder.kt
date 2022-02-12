package com.nahlasamir244.flickrsearchapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nahlasamir244.flickrsearchapp.databinding.ItemAdBannerBinding

class AdBannerViewHolder(
    private val binding: ItemAdBannerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String) {
        binding.apply {
            textViewAd.text = text
        }
    }

    companion object {
        fun create(parent: ViewGroup): AdBannerViewHolder {
            val binding = ItemAdBannerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return AdBannerViewHolder(binding)
        }
    }
}