package com.example.book_tracker.core.domain.model.book

import android.content.Context
import com.example.book_tracker.R

/**
 * BookShelf is a enum class for Book's field "bookShelf"
 * @see Book
 * */
enum class BookShelf(private val id: Int, private val icon: Int) {
    CurrentRead(R.string.book_current_read, R.drawable.book),
    ToRead(R.string.book_to_read, R.drawable.book),
    Unfinished(R.string.book_unfinished, R.drawable.book),
    Wishlist(R.string.book_wishlist, R.drawable.book),
    Favorite(R.string.book_favorite, R.drawable.ic_round_favorite_24),
    Unclassified(R.string.book_unclassified, R.drawable.book),
    Done(R.string.book_done, R.drawable.ic_round_done_24),
    ;

    fun getLabel(context: Context) =
        context.getString(id)
}