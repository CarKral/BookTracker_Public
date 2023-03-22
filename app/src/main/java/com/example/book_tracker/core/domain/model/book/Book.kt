package com.example.book_tracker.core.domain.model.book

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Book(
    @Exclude var id: String? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,

    var position: Int? = null,

    @get:PropertyName("google_book_id")
    @set:PropertyName("google_book_id")
    var googleBookId: String? = null,

    @get:PropertyName("creator_id")
    @set:PropertyName("creator_id")
    var creatorId: String? = null,

    var title: String? = null,
    var subtitle: String? = null,
    var authors: String? = null,

    @get:PropertyName("book_photo_url")
    @set:PropertyName("book_photo_url")
    var photoUrl: String? = null,

    var categories: String? = null,

    @get:PropertyName("published_date")
    @set:PropertyName("published_date")
    var publishedDate: String? = null,

    var publisher: String? = null,

    @get:PropertyName("added_date")
    @set:PropertyName("added_date")
    var addedDate: Timestamp? = null,

    var rating: Double? = null,
    var note: String? = null,

    @get:PropertyName("borrowed_to")
    @set:PropertyName("borrowed_to")
    var borrowedTo: String? = null,

    @get:PropertyName("borrowed_from")
    @set:PropertyName("borrowed_from")
    var borrowedFrom: String? = null,

    @get:PropertyName("page_count")
    @set:PropertyName("page_count")
    var pageCount: Int? = null,

    @get:PropertyName("print_type")
    @set:PropertyName("print_type")
    var printType: List<String>? = null,

    @get:PropertyName("current_page")
    @set:PropertyName("current_page")
    var currentPage: Double? = 0.0,

    @get:PropertyName("book_shelf")
    @set:PropertyName("book_shelf")
    var bookShelf: String? = null,

    @get:PropertyName("started_reading_at")
    @set:PropertyName("started_reading_at")
    var startedReading: Timestamp? = null,

    @get:PropertyName("finished_reading_at")
    @set:PropertyName("finished_reading_at")
    var finishedReading: Timestamp? = null,

    @get:PropertyName("last_reading")
    @set:PropertyName("last_reading")
    var lastReading: @RawValue BookReading? = null,

    @get:PropertyName("goal_reading_speed")
    @set:PropertyName("goal_reading_speed")
    var goalReadingSpeed: Double? = null,

) : Parcelable