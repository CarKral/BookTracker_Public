package com.example.book_tracker.features.feature_auth.presentation

import com.example.book_tracker.core.domain.model.user.MyUser
import com.google.firebase.auth.FirebaseUser

data class AuthState(
    val uiState: UiState = UiState.Idle,
    val myUser: MyUser? = null,
    val firebaseUser: FirebaseUser? = null,
) {
    sealed interface UiState {
        object Idle : UiState
        object Loading : UiState
        object CreatingUser : UiState

        data class Success(val data: Boolean) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}