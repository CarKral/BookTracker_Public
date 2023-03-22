package com.example.book_tracker.features.feature_user_list.presentation

import com.example.book_tracker.core.domain.model.user.MyUser

data class UserListState(
    val uiState: UiState = UiState.Loading,
    val users: List<MyUser>? = null,
){
    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: List<MyUser>?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}