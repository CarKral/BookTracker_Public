package com.example.book_tracker.features.feature_book_list.presentation.user_book_list

import com.example.book_tracker.core.domain.model.book.Book

data class UserBookListState(
    val uiState: UiState = UiState.Loading,
    val userId: String? = null,
    val bookShelf: String? = null,
    val books: List<Book>? = null,
) {

    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: List<Book>?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}