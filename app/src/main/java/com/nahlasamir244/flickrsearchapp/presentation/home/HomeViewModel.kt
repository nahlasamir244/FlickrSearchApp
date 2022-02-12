package com.nahlasamir244.flickrsearchapp.presentation.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.nahlasamir244.flickrsearchapp.data.model.Ad
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import com.nahlasamir244.flickrsearchapp.data.repo.photo.PhotoRepository
import com.nahlasamir244.flickrsearchapp.domain.InputType
import com.nahlasamir244.flickrsearchapp.domain.IsValid
import com.nahlasamir244.flickrsearchapp.presentation.home.adapter.HomeUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    private val photoRepository: PhotoRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val homeUiState: StateFlow<HomeUiState>
    val photosWithAdsPagingDataFlow: Flow<PagingData<HomeUiModel>>
    val homeUiActions: (HomeUiAction) -> Unit
    val isValid = IsValid()

    init {
        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_SEARCH_QUERY
        val lastQueryScrolled: String =
            savedStateHandle.get(LAST_QUERY_SCROLLED) ?: DEFAULT_SEARCH_QUERY
        val actionStateFlow = MutableSharedFlow<HomeUiAction>()
        val searchAction = actionStateFlow
            .filterIsInstance<HomeUiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(HomeUiAction.Search(searchQuery = initialQuery)) }
        val scrollAction = actionStateFlow
            .filterIsInstance<HomeUiAction.Scroll>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(HomeUiAction.Scroll(currentSearchQuery = lastQueryScrolled)) }

        photosWithAdsPagingDataFlow = searchAction
            .flatMapLatest { getPhotosWithAdBannerSeparators(it.searchQuery) }
            .cachedIn(viewModelScope)

        homeUiState = combine(
            searchAction,
            scrollAction,
            ::Pair
        ).map { (search, scroll) ->
            HomeUiState(
                query = search.searchQuery,
                lastQueryScrolled = scroll.searchQuery,
                hasNotScrolledForCurrentSearch = search.searchQuery != scroll.searchQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = REQUEST_TIMEOUT),
                initialValue = HomeUiState(
                    DEFAULT_SEARCH_QUERY, DEFAULT_SEARCH_QUERY,
                    true
                )
            )

        homeUiActions = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    private fun getPhotosWithAdBannerSeparators(searchQuery: String):
            Flow<PagingData<HomeUiModel>> {

        return searchPhotos(searchQuery)
            .map { pagingData -> pagingData.map { HomeUiModel.PhotoItem(it) } }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null) {
                        //last item
                        return@insertSeparators null
                    }

                    if (before == null) {
                        //first item
                        return@insertSeparators null
                    }
                    if ((before.photo.index + 1).rem(5) == 0) {
                        HomeUiModel.AdBannerItem(Ad(AD_UNIT_ID))
                    } else {
                        null
                    }

                }
            }
    }

    private fun searchPhotos(searchQuery: String): Flow<PagingData<Photo>> =
        photoRepository.getSearchResultPhotoListStream(searchQuery)

    fun isValidQueryInput(text: String) = isValid(text, InputType.PHOTO_SEARCH_QUERY)

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = homeUiState.value.query
        savedStateHandle[LAST_QUERY_SCROLLED] = homeUiState.value.lastQueryScrolled
        super.onCleared()
    }

    companion object {
        private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_SEARCH_QUERY = "color"
        private const val REQUEST_TIMEOUT: Long = 5000
        private const val AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }

}