package com.example.book_tracker.core.domain.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

interface FirestoreRepository {
    val usersCollection: CollectionReference
    val userDocumentReference: DocumentReference
    val userBookCollection: CollectionReference
//    val userNoteCollection: CollectionReference
//    val userReadingCollection: CollectionReference

    fun userNotesCollection(bookId: String): CollectionReference
    fun userReadingsCollection(bookId: String): CollectionReference

}