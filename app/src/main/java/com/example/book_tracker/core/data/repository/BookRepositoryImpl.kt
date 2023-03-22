package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.repository.BookRepository
import com.example.book_tracker.core.domain.repository.FirestoreRepository
import com.example.book_tracker.core.presentation.util.Constants
import com.example.book_tracker.core.presentation.util.Constants.FIRESTORE_BOOKS_COLLECTION
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) : BookRepository {
//    private val usersCollection = firebaseFirestore.collection(FIRESTORE_USERS_COLLECTION)
//    private val booksCollection = usersCollection.document(currentUserId ?: "").collection(FIRESTORE_BOOKS_COLLECTION)

    /**
     * @return Flow of books from Database
     * */
    override suspend fun getBooks(userId: String, bookShelf: String?, limit: Long?): Flow<List<Book>> {
        val collection = firestoreRepository.usersCollection.document(userId).collection(FIRESTORE_BOOKS_COLLECTION)
        var query =
            bookShelf?.let { collection.whereEqualTo(Constants.FIRESTORE_BOOKSHELF_FIELD, bookShelf) } ?: collection
        query = limit?.let { query.limit(limit) } ?: query
        query = query.orderBy(Constants.FIRESTORE_ADDED_DATE_FIELD, Query.Direction.DESCENDING)
        return query.snapshots().map { value: QuerySnapshot -> value.toObjects(Book::class.java) }
    }

    override suspend fun addBook(
        book: Book,
        onSuccess: (book: Book?) -> Unit,
        onExist: (Boolean) -> Unit
    ) {
        if (bookAlreadyExist(book) == true) {
            onExist(true)
            return
        }
        if (book.id == null) book.id = firestoreRepository.userBookCollection.document().id

        book.id?.let {
            book.addedDate = Timestamp.now()
            firestoreRepository.userBookCollection.document(it).set(book).addOnSuccessListener {
                onSuccess(book)
            }.await()
        }
    }

    override suspend fun getBook(userId: String, bookId: String): Flow<Book?> {
        val collection = firestoreRepository.usersCollection.document(userId).collection(FIRESTORE_BOOKS_COLLECTION)
        return collection.document(bookId).snapshots().map { value -> value.toObject(Book::class.java) }
    }

    override suspend fun updateBook(book: Book, onSuccess: (book: Book?) -> Unit) {
        book.id?.let {
            firestoreRepository.userBookCollection.document(it).set(book).addOnSuccessListener {
                onSuccess(book)
            }.await()
        }
    }

    override suspend fun updateBookPosition(book: Book, onSuccess: (book: Book?) -> Unit) {
        book.id?.let {
            firestoreRepository.userBookCollection.document(it).update("position", book.position).addOnSuccessListener {
                onSuccess(book)
            }.await()
        }
    }

    override suspend fun deleteBook(id: String, onSuccess: () -> Unit) {
        firestoreRepository.userBookCollection.document(id).delete().addOnSuccessListener {
            onSuccess()
        }.await()
    }

    override suspend fun addNewReadingGoal(book: Book?, onSuccess: (book: Book?) -> Unit) {
        /** myBook.goalSpeed, myBook.goalFinished */
        book?.id?.let { id ->
            firestoreRepository.userBookCollection.document(id)
                .update(
                    "goal_reading_speed", book.goalReadingSpeed,
//                    "goal_finished", myBook.goalFinished,
//                    "goal_finished_date", myBook.goalFinishedDate
                )
                .addOnSuccessListener {
                    onSuccess(book)
                    Timber.e(book.toString())

                }
        }
    }

    override suspend fun deleteReadingGoal(id: String?, onSuccess: () -> Unit) {
        id?.let { it ->
            firestoreRepository.userBookCollection.document(id).update("goal_reading_speed", null)
                .addOnSuccessListener { onSuccess() }
        }
    }

    private suspend fun bookAlreadyExist(book: Book?): Boolean? {
        val id = book?.googleBookId ?: book?.id
        val field = if (book?.googleBookId != null) "google_book_id" else "id"

        id?.let {
            var exist: Boolean? = null
            firestoreRepository.userBookCollection.whereEqualTo(field, it).get()
                .addOnSuccessListener { document ->
                    exist = !document.isEmpty
                }
                .addOnFailureListener { exist = null }
                .await()

            return exist
        }
        return null
    }
}