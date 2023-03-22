package com.example.book_tracker.core.domain.repository

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookReading
import kotlinx.coroutines.flow.Flow

interface ReadingRepository {
    suspend fun getReadingList(bookId: String): Flow<List<BookReading>>

    suspend fun addNewReading(book: Book?, reading: BookReading, onSuccess: (reading: BookReading?) -> Unit)
    suspend fun updateReading(reading: BookReading?, onSuccess: (reading: BookReading?) -> Unit)
    suspend fun resetReading(book: Book?, reading: BookReading?, onSuccess: () -> Unit)

}