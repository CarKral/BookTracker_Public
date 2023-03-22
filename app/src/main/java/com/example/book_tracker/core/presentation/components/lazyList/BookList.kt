package com.example.book_tracker.core.presentation.components.lazyList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.book_tracker.core.domain.model.book.Book

/**
 * BookList is used as LazyList at BookListScreen, HomeScreen,
 */
@Composable
fun BookList(
    books: List<Book>,
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    item: @Composable (Book) -> Unit
) {
    LazyColumn(
        state = lazyColumnState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(books) {
            item(it)
        }
    }
}