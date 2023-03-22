package com.example.book_tracker.core.domain.use_case.book

import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.asResource
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

/** USE CASE for getting list of Books from Database */
class GetBookListUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookRepository: BookRepository,
) {

    suspend operator fun invoke(userId: String?, bookShelf: String?, limit: Long?): Flow<Resource<List<Book>>> {
        val id = userId ?: authRepository.currentUserId
        Timber.d("USER ID: $id")
        return if (id != null) {
            bookRepository.getBooks(id, bookShelf, limit).asResource()
        } else flow { Resource.Error(Throwable("Current Firebase user ID is null")) }
    }
}