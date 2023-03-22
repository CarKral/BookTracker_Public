package com.example.book_tracker.features.feature_library.presentation.user_library

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.user.MyUser

data class UserLibraryState(
    val uiState: UiState = UiState.Loading,
    val userId: String? = null,
    val myUser: MyUser? = null,
    val libraryBooks: MutableMap<String, List<Book>> = mutableMapOf()
){
    sealed interface UiState {
        object Loading : UiState
        object Success : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}