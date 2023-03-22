package com.example.book_tracker.features.feature_search.presentation

import com.example.book_tracker.core.domain.model.book.Item

data class SearchState(
    val uiState: UiState = UiState.Idle,
    val items: List<Item>? = null,
    val searchQuery: String? = null,
    val orderByNewest: Boolean = false,
) {

    sealed interface UiState {
        object Idle : UiState
        object Loading : UiState
        data class Success(val data: List<Item>?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}