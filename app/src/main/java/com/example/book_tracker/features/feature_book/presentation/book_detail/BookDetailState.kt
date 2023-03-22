package com.example.book_tracker.features.feature_book.presentation.book_detail

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookReading

data class BookDetailState(
    val uiState: UiState = UiState.Loading,
    val bookId: String? = null,
    val book: Book? = null,
    val readings: List<BookReading>? = null,
    val myReading: BookReading? = null,
    val newPage: String? = null,
) {

    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: Book?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}