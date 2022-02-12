package com.nahlasamir244.flickrsearchapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.nahlasamir244.flickrsearchapp.databinding.ItemAdBannerBinding


class AdBannerViewHolder(
    private val binding: ItemAdBannerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.apply {
            val adRequest: AdRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
            adView.adListener = AdBannerHandler
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