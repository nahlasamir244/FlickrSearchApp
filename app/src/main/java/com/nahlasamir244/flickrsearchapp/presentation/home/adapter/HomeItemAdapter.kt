package com.nahlasamir244.flickrsearchapp.presentation.home.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.nahlasamir244.flickrsearchapp.R
import com.nahlasamir244.flickrsearchapp.utils.exhaustive

class HomeItemAdapter(private val photoAdapterHandler: PhotoAdapterHandler) :
    PagingDataAdapter<HomeUiModel, ViewHolder>(HOME_UI_MODEL_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == R.layout.item_photo) {
            PhotoViewHolder.create(parent, photoAdapterHandler)
        } else {
            AdBannerViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeUiModel.PhotoItem -> R.layout.item_photo
            is HomeUiModel.AdBannerItem -> R.layout.item_ad_banner
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val homeUiModel = getItem(position)
        homeUiModel.let {
            when (homeUiModel) {
                is HomeUiModel.PhotoItem ->
                    (holder as PhotoViewHolder).bind(homeUiModel.photo)
                is HomeUiModel.AdBannerItem ->
                    (holder as AdBannerViewHolder).bind(homeUiModel.text)
                else -> {
                }
            }.exhaustive
        }
    }

    companion object {
        private val HOME_UI_MODEL_COMPARATOR =
            object : DiffUtil.ItemCallback<HomeUiModel>() {
                override fun areItemsTheSame(oldItem: HomeUiModel, newItem: HomeUiModel): Boolean {
                    return (oldItem is HomeUiModel.PhotoItem && newItem is HomeUiModel.PhotoItem &&
                            oldItem.photo.id == newItem.photo.id) ||
                            (oldItem is HomeUiModel.AdBannerItem &&
                                    newItem is HomeUiModel.AdBannerItem &&
                                    oldItem.text == newItem.text)
                }

                override fun areContentsTheSame(oldItem: HomeUiModel, newItem: HomeUiModel)
                        : Boolean = oldItem == newItem
            }
    }
}
