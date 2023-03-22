package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.fakeBook
import com.example.book_tracker.core.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeBookRepositoryImpl() : BookRepository {
    override suspend fun getBooks(userId: String, bookShelf: String?, limit: Long?): Flow<List<Book>> {
        return flow {
            listOf<Book>(
                fakeBook, fakeBook, fakeBook, fakeBook, fakeBook, fakeBook
            )
        }
    }

    override suspend fun addBook(book: Book, onSuccess: (book: Book?) -> Unit, onExist: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getBook(userId: String, bookId: String): Flow<Book?> {
        return flow { fakeBook }
    }

    override suspend fun updateBook(book: Book, onSuccess: (book: Book?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun updateBookPosition(book: Book, onSuccess: (book: Book?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBook(id: String, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun addNewReadingGoal(book: Book?, onSuccess: (book: Book?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReadingGoal(id: String?, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

}
