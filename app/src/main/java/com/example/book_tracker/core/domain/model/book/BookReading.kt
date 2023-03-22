package com.example.book_tracker.core.domain.model.book

import com.google.firebase.Timestamp

/**
 * BookReading is a data class for Book's Reading item and for Book's field "lastReading"
 * @see Book
 * */
data class BookReading(
    var id: String? = null,
//    var bookId: String? = null,
//    var pages: Double? = null,
//    var pageRange: String? = null,
    var pageRangeList: List<Double?>? = null,
    var date: Timestamp? = null,
)