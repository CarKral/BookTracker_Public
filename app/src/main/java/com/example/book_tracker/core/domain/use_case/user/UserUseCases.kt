package com.example.book_tracker.core.domain.use_case.user

/** Wrapper data class of all User use cases */
data class UserUseCases(
    val addUserUseCase: AddUserUseCase,
    val getUserUseCase: GetUserUseCase,
    val getUserListUseCase: GetUserListUseCase
)
