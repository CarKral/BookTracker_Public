package com.example.book_tracker.core.domain.repository

import com.example.book_tracker.core.domain.model.book.BookNote
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun getNoteList(bookId: String): Flow<List<BookNote>>

    suspend fun addNote(note: BookNote?, onSuccess: (note: BookNote?) -> Unit)
    suspend fun getNote(userId: String, bookId: String): Flow<BookNote?>
    suspend fun updateNote(note: BookNote?, onSuccess: (note: BookNote?) -> Unit)
    suspend fun deleteNote(id: String, onSuccess: () -> Unit)
}