package com.example.book_tracker.core.domain.use_case.auth

/** Wrapper data class of all Auth use cases */
data class AuthUseCases(
    val signInWithCredentialsUseCase: SignInWithCredentialsUseCase,
    val getAuthUserUseCase: GetAuthUserUseCase
)
