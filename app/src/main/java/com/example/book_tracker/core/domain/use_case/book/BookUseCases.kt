package com.example.book_tracker.core.domain.use_case.book

/** Wrapper data class of all Book and Item use cases */
data class BookUseCases(
    val addBookUseCase: AddBookUseCase,
    val getItemUseCase: GetItemUseCase,
    val getItemListUseCase: GetItemListUseCase,
    val getBookUseCase: GetBookUseCase,
    val getBookListUseCase: GetBookListUseCase,
)
