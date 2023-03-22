package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.repository.FirestoreRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

class FakeFirestoreRepositoryImpl() : FirestoreRepository {
    override val usersCollection: CollectionReference
        get() = TODO("Not yet implemented")
    override val userDocumentReference: DocumentReference
        get() = TODO("Not yet implemented")
    override val userBookCollection: CollectionReference
        get() = TODO("Not yet implemented")

    override fun userNotesCollection(bookId: String): CollectionReference {
        TODO("Not yet implemented")
    }

    override fun userReadingsCollection(bookId: String): CollectionReference {
        TODO("Not yet implemented")
    }

}
