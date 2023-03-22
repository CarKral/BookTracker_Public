package com.example.book_tracker.features.feature_library.presentation.my_library

import com.example.book_tracker.core.domain.model.book.Book

data class LibraryState(
    val uiState: UiState = UiState.Loading,
    val libraryBooks: MutableMap<String, List<Book>> = mutableMapOf()
){
    sealed interface UiState {
        object Loading : UiState
        object Success : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}