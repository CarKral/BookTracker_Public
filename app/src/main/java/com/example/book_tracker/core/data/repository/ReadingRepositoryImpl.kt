package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookReading
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.repository.FirestoreRepository
import com.example.book_tracker.core.domain.repository.ReadingRepository
import com.example.book_tracker.core.presentation.util.todayAsStartOfDay
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ReadingRepositoryImpl @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
) : ReadingRepository {

    override suspend fun getReadingList(bookId: String): Flow<List<BookReading>> {
        val readingCollection =
            firestoreRepository.userReadingsCollection(bookId)

        return readingCollection
            .orderBy("date", Query.Direction.DESCENDING).snapshots()
            .map { value: QuerySnapshot -> value.toObjects(BookReading::class.java) }

    }

    override suspend fun addNewReading(
        book: Book?,
        reading: BookReading,
        onSuccess: (reading: BookReading?) -> Unit
    ) {
        book?.id?.let { bookId ->
            val readingCollection = firestoreRepository.userReadingsCollection(bookId)

            reading.id = readingCollection.document().id
            val pageCount = book.pageCount?.toDouble()
            var newBookShelf = book.bookShelf
            val newCurrentPage = book.currentPage
            var finishedReading: Timestamp? = null

            if (newCurrentPage == pageCount) {
                newBookShelf = BookShelf.Done.toString()
                finishedReading = Timestamp.now()
            }

            reading.id?.let { readingId ->
                reading.date = Timestamp.now()
                readingCollection.document(readingId).set(reading).addOnSuccessListener {
                    onSuccess(reading)

//                Timber.e(myBook.toString())
                    book.id?.let { bookId ->
                        firestoreRepository.userBookCollection.document(bookId)
                            .update(
                                "book_shelf", newBookShelf,
                                "last_reading", reading,
//                            "reading_speed", readingSpeed,
                                "current_page", newCurrentPage,
                                "finished_reading_at", finishedReading
                            )
                            .addOnSuccessListener {
                                Timber.e(reading.toString())
                            }
                    }
                }.await()
            }


        }
    }

    override suspend fun updateReading(reading: BookReading?, onSuccess: (reading: BookReading?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun resetReading(book: Book?, reading: BookReading?, onSuccess: () -> Unit) {

        // reset údajů ohledně čtení u knihy
        book?.id?.let { id ->
            firestoreRepository.userBookCollection.document(id)
                .update(
                    "started_reading_at", todayAsStartOfDay,
                    "finished_reading_at", null,
                    "last_reading", null,
                    "reading_speed", null,
                    "current_page", 0,
                    "goal_reading_speed", null,
                    "goal_finished_date", null,
                )
                .addOnSuccessListener {
                    onSuccess()
//                    userBookCollection.document(id).collection(READING_COLLECTION)
//                    Timber.e(reading.toString())
                }.await()
//            userReadingCollection.whereEqualTo("bookId", id).delete()
        }


        // smazání údajů jednotlivých čtení pro knihu


//        TODO("SMAZAT VŠECHNA ČTENÍ U KNIHY")
//        TODO("SMAZAT ÚDAJE O ČTENÍ U KNIHY")
    }


}