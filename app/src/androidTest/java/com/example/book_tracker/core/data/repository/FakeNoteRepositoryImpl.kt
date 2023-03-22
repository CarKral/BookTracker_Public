package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.model.book.BookNote
import com.example.book_tracker.core.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class FakeNoteRepositoryImpl(): NoteRepository {
    override suspend fun getNoteList(bookId: String): Flow<List<BookNote>> {
        TODO("Not yet implemented")
    }

    override suspend fun addNote(note: BookNote?, onSuccess: (note: BookNote?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getNote(userId: String, bookId: String): Flow<BookNote?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateNote(note: BookNote?, onSuccess: (note: BookNote?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(id: String, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }


}
