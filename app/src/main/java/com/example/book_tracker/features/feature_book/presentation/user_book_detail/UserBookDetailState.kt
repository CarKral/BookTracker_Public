package com.example.book_tracker.features.feature_book.presentation.user_book_detail

import com.example.book_tracker.core.domain.model.book.Book

data class UserBookDetailState(
    val uiState: UiState = UiState.Loading,
    val userId: String? = null,
    val bookId: String? = null,
    val book: Book? = null,
    val isAddedToLibrary: Boolean = false,
    val newPage: String? = null,
){
    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: Book?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}