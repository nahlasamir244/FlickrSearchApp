package com.nahlasamir244.flickrsearchapp.data.repo.photo

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nahlasamir244.flickrsearchapp.data.datasource.photo.page.PhotoRemotePagingSource
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImpl: PhotoRepository {
    override fun getSearchResultPhotoListStream(searchQuery: String): Flow<PagingData<Photo>> {
        Log.d("PhotoRepository", "Search query: $searchQuery")
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { PhotoRemotePagingSource(searchQuery) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
}