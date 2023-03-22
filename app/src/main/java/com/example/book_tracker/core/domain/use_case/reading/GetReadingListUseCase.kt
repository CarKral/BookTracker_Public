package com.example.book_tracker.core.domain.use_case.reading

import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.asResource
import com.example.book_tracker.core.domain.model.book.BookReading
import com.example.book_tracker.core.domain.repository.ReadingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** USE CASE for getting list of Reading from Database */
class GetReadingListUseCase @Inject constructor(
    private val readingRepository: ReadingRepository
) {

    suspend operator fun invoke(bookId: String): Flow<Resource<List<BookReading>>> {
        return  readingRepository.getReadingList(bookId).asResource()
    }
}