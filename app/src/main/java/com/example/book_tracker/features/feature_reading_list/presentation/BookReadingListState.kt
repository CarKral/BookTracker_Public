package com.example.book_tracker.features.feature_reading_list.presentation

import com.example.book_tracker.core.domain.model.book.BookReading

data class BookReadingListState(
    val uiState: UiState = UiState.Loading,
    val bookId: String? = null,
    val readings: List<BookReading>? = null,
){
    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: List<BookReading>?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}