package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.model.book.BookNote
import com.example.book_tracker.core.domain.repository.FirestoreRepository
import com.example.book_tracker.core.domain.repository.NoteRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : NoteRepository {

    override suspend fun getNoteList(bookId: String): Flow<List<BookNote>> {
        val notesCollection = firestoreRepository.userNotesCollection(bookId)
        return notesCollection.whereEqualTo("bookId", bookId).snapshots()
            .map { value: QuerySnapshot -> value.toObjects(BookNote::class.java) }
    }

    override suspend fun addNote(note: BookNote?, onSuccess: (note: BookNote?) -> Unit) {
        note?.bookId?.let { bookId ->
            val notesCollection = firestoreRepository.userNotesCollection(bookId)
//            val notesCollection = booksCollection.document(bookId).collection(FIRESTORE_NOTES_COLLECTION)
            note.id = notesCollection.document().id
            note.id?.let { id ->
                note.date = Timestamp.now()
                notesCollection.document(id).set(note).addOnSuccessListener {
                    onSuccess(note)
                }.await()
            }
        }
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