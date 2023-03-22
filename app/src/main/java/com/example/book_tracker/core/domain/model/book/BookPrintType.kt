package com.example.book_tracker.core.domain.model.book

import android.content.Context
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.BookPrintType.Book

/**
 * BookPrintType is a enum class for Book's field "printType"
 * @see Book
 * */
enum class BookPrintType(private val id: Int, private val icon: Int) {
    Book(R.string.book, R.drawable.ic_book_type),
    Ebook(R.string.e_book, R.drawable.ic_ebook_type),
    AudioBook(R.string.audiobook, R.drawable.ic_audiobook_type),
    ;

    fun getLabel(context: Context) =
        context.getString(id)

    fun getIcon() = icon
}