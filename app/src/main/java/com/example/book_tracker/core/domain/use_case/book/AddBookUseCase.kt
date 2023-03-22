package com.example.book_tracker.core.domain.use_case.book

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.repository.BookRepository
import javax.inject.Inject

/** USE CASE for adding Book to Database */
class AddBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(book: Book, onSuccess: (Book?) -> Unit, onExist: (Boolean) -> Unit) {
        bookRepository.addBook(
            book = book,
            onSuccess = {
                println("Added at database complete")
                onSuccess(it)

            }, onExist = {
                println("Book already exist in database")
                onExist(it)
            }
        )
    }
}