package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.repository.FirestoreRepository
import com.example.book_tracker.core.presentation.util.Constants.FIRESTORE_BOOKS_COLLECTION
import com.example.book_tracker.core.presentation.util.Constants.FIRESTORE_NOTES_COLLECTION
import com.example.book_tracker.core.presentation.util.Constants.FIRESTORE_READING_COLLECTION
import com.example.book_tracker.core.presentation.util.Constants.FIRESTORE_USERS_COLLECTION
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestore: FirebaseFirestore,
) : FirestoreRepository {

    /** @return Firestore "users" collection reference */
    override val usersCollection: CollectionReference
        get() = firestore.collection(FIRESTORE_USERS_COLLECTION)


    /** @return Firestore CURRENT USER document reference */
    override val userDocumentReference: DocumentReference
        get() = usersCollection.document(authRepository.currentUserId ?: "")

    /** @return Firestore CURRENT USER "books" collection reference */
    override val userBookCollection: CollectionReference
        get() = userDocumentReference.collection(FIRESTORE_BOOKS_COLLECTION)

//    /** @return Firestore CURRENT USER "notes" collection reference */
//    override val userNoteCollection: CollectionReference
//        get() = userDocumentReference.collection(FIRESTORE_NOTES_COLLECTION)
//
//    /** @return Firestore CURRENT USER "readings" collection reference */
//    override val userReadingCollection: CollectionReference
//        get() = userDocumentReference.collection(FIRESTORE_READING_COLLECTION)


    override fun userNotesCollection(bookId: String): CollectionReference {
        return userDocumentReference
            .collection(FIRESTORE_BOOKS_COLLECTION)
            .document(bookId)
            .collection(FIRESTORE_NOTES_COLLECTION)
    }

    override fun userReadingsCollection(bookId: String): CollectionReference {
        return userDocumentReference
            .collection(FIRESTORE_BOOKS_COLLECTION)
            .document(bookId)
            .collection(FIRESTORE_READING_COLLECTION)
    }


}

