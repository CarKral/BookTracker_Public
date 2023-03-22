package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookReading
import com.example.book_tracker.core.domain.repository.ReadingRepository
import kotlinx.coroutines.flow.Flow

class FakeReadingRepositoryImpl() : ReadingRepository {
    override suspend fun getReadingList(bookId: String): Flow<List<BookReading>> {
        TODO("Not yet implemented")
    }

    override suspend fun addNewReading(
        book: Book?,
        reading: BookReading,
        onSuccess: (reading: BookReading?) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateReading(reading: BookReading?, onSuccess: (reading: BookReading?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun resetReading(book: Book?, reading: BookReading?, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

}
