package com.example.book_tracker.core.domain.use_case.note

import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.asResource
import com.example.book_tracker.core.domain.model.book.BookNote
import com.example.book_tracker.core.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** USE CASE for getting list of Notes from Database */
class GetNoteListUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    suspend operator fun invoke(bookId: String): Flow<Resource<List<BookNote>>> {
        return noteRepository.getNoteList(bookId).asResource()
    }
}