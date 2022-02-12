package com.nahlasamir244.flickrsearchapp.presentation.home

sealed class HomeUiAction(val searchQuery: String) {
    class Search(searchQuery: String) : HomeUiAction(searchQuery)
    class Scroll(currentSearchQuery: String) : HomeUiAction(currentSearchQuery)
}