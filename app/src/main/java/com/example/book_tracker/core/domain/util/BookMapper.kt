package com.example.book_tracker.core.domain.util

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookPrintType
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.model.book.Item

/** Extension function for creating new Book from given Item
 * - Used at SearchScreen and BookDetailSearchScreen when user is adding Book from search
 * */
fun Item?.mapToMewBook(userId: String?): Book {
    return Book(
        title = this?.volumeInfo?.title,
        subtitle = this?.volumeInfo?.subtitle,
        authors = this?.volumeInfo?.authors?.joinToString(),
        categories = this?.volumeInfo?.categories?.joinToString(),
        photoUrl = this?.volumeInfo?.imageLinks?.thumbnail ?: this?.volumeInfo?.imageLinks?.smallThumbnail,
        publishedDate = this?.volumeInfo?.publishedDate,
        publisher = this?.volumeInfo?.publisher,
        pageCount = this?.volumeInfo?.pageCount,
        googleBookId = this?.id,
        userId = userId,
        // Initial value for BookShelf
        bookShelf = BookShelf.Unclassified.toString(),
        // Initial value for printType
        printType = listOf(BookPrintType.Book.toString())
    )
}

/** Extension function for creating new Book from other Book
 * - Used at UserBookDetailScreen when user is adding Book from other user library
 * */
fun Book?.mapToMewBook(userId: String?): Book {
    return Book(
        title = this?.title,
        subtitle = this?.subtitle,
        authors = this?.authors,
        categories = this?.categories,
        photoUrl = this?.photoUrl,
        publishedDate = this?.publishedDate,
        publisher = this?.publisher,
        pageCount = this?.pageCount,
        creatorId = this?.creatorId,
        userId = userId,
        // Initial value for BookShelf
        bookShelf = BookShelf.Unclassified.toString(),
        // Initial value for printType f
        printType = listOf(BookPrintType.Book.toString())
    )
}