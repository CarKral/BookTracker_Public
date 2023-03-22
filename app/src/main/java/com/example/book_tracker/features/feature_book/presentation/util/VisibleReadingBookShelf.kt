package com.example.book_tracker.features.feature_book.presentation.util

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookShelf

fun visibleReadingBookShelf(book: Book): Boolean {
    return if (book.pageCount == null) false
    else when (book.bookShelf) {
        BookShelf.CurrentRead.toString(),
        BookShelf.Unfinished.toString(),
        BookShelf.Done.toString() -> true
        else -> false
    }
}