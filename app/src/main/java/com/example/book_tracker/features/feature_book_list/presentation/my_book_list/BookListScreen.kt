package com.example.book_tracker.features.feature_book_list.presentation.my_book_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.presentation.components.Loading
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.features.feature_book_list.presentation.components.BookListScreenContent
import com.example.book_tracker.features.feature_book_list.presentation.components.BookListScreenTopAppBar
import com.example.book_tracker.features.feature_book_list.presentation.my_book_list.BookListState.UiState


@Composable
fun BookListScreen(
    state: BookListState,
    viewModel: BookListScreenViewModel,
    popUp: () -> Unit,
    openScreen: (String) -> Unit,
) {

    val lazyColumnState = rememberLazyListState()
    val bookShelfString = state.bookShelf?.let { BookShelf.valueOf(it).getLabel(LocalContext.current) }

    LaunchedEffect(Unit) {
        viewModel.getBooks()
    }

    Scaffold(
        topBar = { BookListScreenTopAppBar(bookShelfString, popUp, openScreen) },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            when (state.uiState) {
                is UiState.Loading ->  Loading()
                is UiState.Success -> {
                    if (state.books != null) {
                        BookListScreenContent(
                            bookShelfString = bookShelfString,
                            books = state.books,
                            lazyColumnState = lazyColumnState,
                            onBookItemClick = {
                                openScreen(BookTrackerScreens.BookDetailScreen.route + "?bookId=${it}")
                            })
                    } else {
                        Text(text = "V této poličcce nejsou momentálně žádné knihy.")
                    }
                }
                is UiState.Error -> {
                    Text(text = "Error: ${state.uiState.throwable}")
                }
            }
        }
    }
}

