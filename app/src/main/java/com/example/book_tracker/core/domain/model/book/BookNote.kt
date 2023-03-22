package com.example.book_tracker.core.domain.model.book

import com.google.firebase.Timestamp

/**
 * BookNote is a data class for Book's note
 * CURRENTLY NOT USED
 * */
data class BookNote(
    var id: String? = null,
    var bookId: String? = null,
    var text: String? = null,
    var date: Timestamp? = null
)