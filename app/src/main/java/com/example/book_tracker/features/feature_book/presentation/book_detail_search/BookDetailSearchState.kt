package com.example.book_tracker.features.feature_book.presentation.book_detail_search

import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.domain.model.book.Book

data class BookDetailSearchState(
    val uiState: UiState = UiState.Loading,
    val bookId: String? = null,
    val item: Item? = null,
    val book: Book? = null,
    val isAddedToLibrary: Boolean = false,
    val newPage: String? = "0",
) {
    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: Item?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}