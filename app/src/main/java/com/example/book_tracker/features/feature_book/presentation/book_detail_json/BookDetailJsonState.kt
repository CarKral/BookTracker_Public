package com.example.book_tracker.features.feature_book.presentation.book_detail_json

import com.example.book_tracker.core.domain.model.book.Item

data class BookDetailJsonState(
    val uiState: UiState? = null,
    val bookId: String? = null,
    val item: Item? = null,
) {
    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: Item?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}