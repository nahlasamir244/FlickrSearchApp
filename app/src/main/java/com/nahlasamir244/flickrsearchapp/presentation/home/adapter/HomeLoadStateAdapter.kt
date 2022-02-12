package com.nahlasamir244.flickrsearchapp.presentation.home.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class HomeLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<HomeLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: HomeLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState)
            : HomeLoadStateViewHolder {
        return HomeLoadStateViewHolder.create(parent, retry)
    }
}