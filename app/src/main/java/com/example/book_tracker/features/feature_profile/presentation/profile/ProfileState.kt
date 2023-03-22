package com.example.book_tracker.features.feature_profile.presentation.profile

import com.example.book_tracker.core.domain.model.user.MyUser
import com.google.firebase.auth.FirebaseUser

data class ProfileState(
    val uiState: UiState = UiState.Loading,
    val user: FirebaseUser? = null,
    val myUser: MyUser? = null,
) {
    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: Any?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}