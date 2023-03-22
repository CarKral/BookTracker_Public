package com.example.book_tracker.features.feature_book_list.presentation.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.presentation.components.item.BookItem
import com.example.book_tracker.core.presentation.components.lazyList.BookList

@Composable
fun BookListScreenContent(
    bookShelfString: String?,
    books: List<Book>,
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    onBookItemClick: (String?) -> Unit
) {
    BookList(
        books = books,
        lazyColumnState = lazyColumnState
    ) {
        BookItem(
            book = it,
            onBookItemClick = {
                onBookItemClick(it.id)
            }
        )
    }
}