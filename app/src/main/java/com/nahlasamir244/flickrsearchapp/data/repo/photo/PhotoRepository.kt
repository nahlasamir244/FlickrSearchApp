package com.nahlasamir244.flickrsearchapp.data.repo.photo

import androidx.paging.PagingData
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getSearchResultPhotoListStream(searchQuery: String): Flow<PagingData<Photo>>
}