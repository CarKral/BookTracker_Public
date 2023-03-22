package com.example.book_tracker.features.feature_home.presentation

import com.example.book_tracker.core.domain.model.book.Book

data class HomeState(
    val uiState: UiState = UiState.Loading,
    val books: List<Book>? = emptyList(),
) {
    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: List<Book>?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}