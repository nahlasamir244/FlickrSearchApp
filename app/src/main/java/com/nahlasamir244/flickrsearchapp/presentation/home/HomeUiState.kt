package com.nahlasamir244.flickrsearchapp.presentation.home

data class HomeUiState(
    val query: String,
    val lastQueryScrolled: String,
    val hasNotScrolledForCurrentSearch: Boolean
)
