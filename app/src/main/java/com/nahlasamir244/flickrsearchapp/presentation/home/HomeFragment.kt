package com.nahlasamir244.flickrsearchapp.presentation.home

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.nahlasamir244.flickrsearchapp.R
import com.nahlasamir244.flickrsearchapp.data.model.Photo
import com.nahlasamir244.flickrsearchapp.databinding.HomeFragmentBinding
import com.nahlasamir244.flickrsearchapp.presentation.home.adapter.HomeItemAdapter
import com.nahlasamir244.flickrsearchapp.presentation.home.adapter.HomeLoadStateAdapter
import com.nahlasamir244.flickrsearchapp.presentation.home.adapter.HomeUiModel
import com.nahlasamir244.flickrsearchapp.presentation.home.adapter.PhotoAdapterHandler
import com.nahlasamir244.flickrsearchapp.utils.helpers.displayToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), PhotoAdapterHandler {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)
        binding.apply {
            viewModel = homeViewModel
            bindState(
                uiState = viewModel!!.homeUiState,
                pagingData = viewModel!!.photosWithAdsPagingDataFlow,
                uiActions = viewModel!!.homeUiActions
            )
        }
    }

    private fun HomeFragmentBinding.bindState(
        uiState: StateFlow<HomeUiState>,
        pagingData: Flow<PagingData<HomeUiModel>>,
        uiActions: (HomeUiAction) -> Unit
    ) {
        val homeItemAdapter = HomeItemAdapter(this@HomeFragment)
        recyclerViewPhotos.adapter = homeItemAdapter.withLoadStateHeaderAndFooter(
            header = HomeLoadStateAdapter {
                homeItemAdapter.retry()
            },
            footer = HomeLoadStateAdapter {
                homeItemAdapter.retry()
            }
        )
        buttonRetry.setOnClickListener {
            homeItemAdapter.retry()
        }

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            homeItemAdapter = homeItemAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }

    private fun HomeFragmentBinding.bindSearch(
        uiState: StateFlow<HomeUiState>,
        onQueryChanged: (HomeUiAction.Search) -> Unit
    ) {
        textInputEditTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateSearchResultFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        textInputEditTextSearch.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateSearchResultFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(textInputEditTextSearch::setText)
        }
    }

    private fun HomeFragmentBinding.updateSearchResultFromInput(onQueryChanged: (HomeUiAction.Search) -> Unit) {
        textInputEditTextSearch.text?.trim().let {
            if (viewModel?.isValidQueryInput(it.toString())==true) {
                recyclerViewPhotos.scrollToPosition(0)
                onQueryChanged(HomeUiAction.Search(searchQuery = it.toString()))
            } else {
                displayToast(R.string.invalid_input_message)
            }
        }
    }

    private fun HomeFragmentBinding.bindList(
        homeItemAdapter: HomeItemAdapter,
        uiState: StateFlow<HomeUiState>,
        pagingData: Flow<PagingData<HomeUiModel>>,
        onScrollChanged: (HomeUiAction.Scroll) -> Unit
    ) {
        recyclerViewPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(
                    HomeUiAction.Scroll
                        (currentSearchQuery = uiState.value.query)
                )
            }
        })
        val notLoading = homeItemAdapter.loadStateFlow
            .distinctUntilChangedBy { it.source.refresh }
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()


        lifecycleScope.launch {
            pagingData.collectLatest(homeItemAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) recyclerViewPhotos.scrollToPosition(0)
            }
        }
        bindLoadState(homeItemAdapter)
    }

    private fun HomeFragmentBinding.bindLoadState(
        homeItemAdapter: HomeItemAdapter
    ) {
        lifecycleScope.launch {
            homeItemAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading
                        && homeItemAdapter.itemCount == 0
                textViewErrorMessage.setText(R.string.no_search_result)
                textViewErrorMessage.isVisible = isListEmpty
                recyclerViewPhotos.isVisible = !isListEmpty
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    displayToast("${it.error.localizedMessage} !!")
                }
            }
        }
    }

    override fun onPhotoClicked(photo: Photo?) {
        navigateToPhotoPreview(photo)
    }
    private fun navigateToPhotoPreview(photo: Photo?) {
        findNavController().navigate(
            HomeFragmentDirections.actionPhotoListFragmentToPhotoPreviewFragment(photo)
        )
    }
}