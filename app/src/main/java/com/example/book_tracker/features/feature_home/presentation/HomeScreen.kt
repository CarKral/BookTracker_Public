package com.example.book_tracker.features.feature_home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.presentation.components.Loading
import com.example.book_tracker.core.presentation.components.item.BookItem
import com.example.book_tracker.core.presentation.components.lazyList.BookList
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.features.feature_home.presentation.HomeState.UiState


@Composable
fun HomeScreen(
    state: HomeState,
    viewModel: HomeScreenViewModel,
    openScreen: (String) -> Unit,
) {
//    val idd = "AXzn6HYhCr2QHhAy2YA3"
//    openScreen(BookTrackerScreens.BookDetailScreen.route + "?bookId=${idd}")

    val lazyColumnState = rememberLazyListState()
//    val books = viewModel.books ?: emptyList()
//
//    val books = viewModel.books?.collectAsState(emptyList())?.value?.sortedByDescending { it.addedDate }
//            ?: emptyList()
//
//    LaunchedEffect(Unit) {
//        viewModel.getBooks()
//    }

    println(state.toString())

    Scaffold(
        topBar = { HomeScreenTopAppBar(openScreen) },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            when (state.uiState) {
                is UiState.Loading -> Loading()

                is UiState.Success -> {
                    if (state.books != null) {
                        HomeScreenContent(
                            books = state.books,
                            lazyColumnState = lazyColumnState,
//                            onPositionChanged = { fromIndex, toIndex -> viewModel.bookPositionChanged(fromIndex, toIndex) },
//                            onDragEnd = {
//                                /*viewModel.bookPositionDragEnd()*/
//                                SnackbarManager.showMessage(R.string.not_complete_warning)  },
                            onBookItemClick = {
                                openScreen(BookTrackerScreens.BookDetailScreen.route + "?bookId=${it}")
                            },
                        )
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

@Composable
fun HomeScreenTopAppBar(
    openScreen: (String) -> Unit
) {
    TopAppBar(
        title = {
            Text("Domů - ${stringResource(R.string.book_current_read)}")
        },

        actions = {
            IconButton(onClick = {
                openScreen(BookTrackerScreens.ProfileScreen.route)
            }) {
                Icon(
                    Icons.Default.Settings,
                    tint = Color.White,
                    contentDescription = "Settings"
                )
            }
        },
    )
}

@Composable
fun HomeScreenContent(
    books: List<Book>,
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    onBookItemClick: (String?) -> Unit,
//    navController: NavController
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
//
//@Composable
//fun BooksList(
//    modifier: Modifier = Modifier,
//    books: List<MyBook>,
//    lazyColumnState: LazyListState,
////    onPositionChanged: (Int, Int) -> Unit,
////    onDragEnd: () -> Unit,
//    onBookItemClick: (String?) -> Unit,
//) {

////    ReorderableLazyColumn(
//        items = books,
//        onMove = { fromIndex, toIndex ->
//            onPositionChanged(fromIndex, toIndex)
////            println("BOOKS          " + books.toString())
////            println("FROM INDEX     "+fromIndex.toString())
////            println("TO INDEX       "+toIndex.toString())
////            items.move(fromIndex, toIndex)
//        },
//        onDragEnd = {
//            onDragEnd()
//        }
//    ) { offset, item ->
//        BookItemHome(
//            offset = offset,
//            book = item as MyBook,
//            onBookItemClick = {
//
////                println("OFFSET          " + offset.toString())
////                println("ITEM          " + item.toString())
////                println("BOOKS          " + reorderableBooks.toList().toString())
//
//                onBookItemClick(item.id)
//            },
//        )
//    }
//}

