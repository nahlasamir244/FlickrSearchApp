package com.nahlasamir244.flickrsearchapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.nahlasamir244.flickrsearchapp.R
import com.nahlasamir244.flickrsearchapp.databinding.ItemLoadStateBinding

class PhotosLoadStateViewHolder(
    private val binding: ItemLoadStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.buttonRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.textViewErrorMessage.text = loadState.error.localizedMessage
        }
        binding.progressbar.isVisible = loadState is LoadState.Loading
        binding.buttonRetry.isVisible = loadState is LoadState.Error
        binding.textViewErrorMessage.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): PhotosLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state, parent, false)
            val binding = ItemLoadStateBinding.bind(view)
            return PhotosLoadStateViewHolder(binding, retry)
        }
    }
}