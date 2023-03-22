package com.example.book_tracker.core.domain.use_case.book

import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.asResource
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/** USE CASE for getting Book from Database */
class GetBookUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookRepository: BookRepository
) {

    suspend operator fun invoke(userId: String?, bookId: String): Flow<Resource<Book?>> {
        val id = userId ?: authRepository.currentUserId
        return if (id != null) {
            bookRepository.getBook(id, bookId).asResource()
        } else flow { Resource.Error(Throwable("Current Firebase user ID is null")) }
    }
}