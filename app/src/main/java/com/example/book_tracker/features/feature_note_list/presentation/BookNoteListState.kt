package com.example.book_tracker.features.feature_note_list.presentation

import com.example.book_tracker.core.domain.model.book.BookNote

data class BookNoteListState(
    val uiState: UiState = UiState.Loading,
    val bookId: String? = null,
    val notes: List<BookNote>? = null,
){
    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: List<BookNote>?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}