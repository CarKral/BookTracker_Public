package com.example.book_tracker.core.domain.repository

import com.example.book_tracker.core.domain.model.book.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getBooks(userId: String, bookShelf: String?, limit: Long?): Flow<List<Book>>

    suspend fun addBook(book: Book, onSuccess: (book: Book?) -> Unit, onExist: (Boolean) -> Unit)
    suspend fun getBook(userId: String, bookId: String): Flow<Book?>
    suspend fun updateBook(book: Book, onSuccess: (book: Book?) -> Unit)
    suspend fun updateBookPosition(book: Book, onSuccess: (book: Book?) -> Unit)
    suspend fun deleteBook(id: String, onSuccess: () -> Unit)

    suspend fun addNewReadingGoal(book: Book?, onSuccess: (book: Book?) -> Unit)
    suspend fun deleteReadingGoal(id: String?, onSuccess: () -> Unit)
}