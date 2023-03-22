package com.example.book_tracker.features.feature_library.presentation.my_library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.book_tracker.R
import com.example.book_tracker.core.presentation.components.LoadingRowWithText
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.model.book.BookPrintType
import com.example.book_tracker.features.feature_library.presentation.my_library.LibraryState.UiState
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.features.feature_library.presentation.components.BookShelfSection


@Composable
fun LibraryScreen(
    state: LibraryState,
    viewModel: LibraryScreenViewModel,
    openScreen: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var selected by remember { mutableStateOf(BookPrintType.Book) }

    Scaffold(
        topBar = {
            LibraryScreenTopAppBar(
                selected = selected,
                openScreen = {
                    openScreen(BookTrackerScreens.BookEditScreen.route + "?bookId=NEW")
                },
                onPrintTypeChanged = { printType ->
                    selected = printType
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
//            Spacer(modifier = Modifier.height(5.dp))

//            when (selected) {
//                MyBookPrintType.Book -> {
            /*for (bookShelf in BookShelf.values()) {
                val books = viewModel.getBooksByBookShelf(bookShelf)
*/
            when (state.uiState) {
                is UiState.Loading -> LoadingRowWithText(text = stringResource(R.string.loading))
                is UiState.Success -> {
                    for (map in state.libraryBooks) {
                        val bookShelf = map.key
                        val books = map.value

                        if (books.isNotEmpty())  {
                            BookShelfSection(
                                BookShelf.valueOf(bookShelf).getLabel(LocalContext.current),
                                books,
                                onMoreBookClick = {
                                    openScreen(BookTrackerScreens.BookListScreen.route + "?bookShelf=${bookShelf}")
                                },
                                onBookItemClick = {
                                    openScreen(BookTrackerScreens.BookDetailScreen.route + "?bookId=${it}")

                                })
                        }
                    }
                }
                is UiState.Error -> {
                    Text(text = "Error: ${state.uiState.throwable}")
                }
            }
//                }
//                MyBookPrintType.EBook -> {
//                    Text(
//                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
//                        text = "Tady není nic k vidění! ZATÍM! 😎",
//                        style = MaterialTheme.typography.h6,
//                        textAlign = TextAlign.Center
//                    )
//                }
//                MyBookPrintType.AudioBook -> {
//                    Text(
//                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
//                        text = "Tady není nic k vidění! ZATÍM! 😎",
//                        style = MaterialTheme.typography.h6,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }
        }
    }
}

@Composable
fun LibraryScreenTopAppBar(
//    viewModel: LibraryScreenViewModel,
//    restartApp: (String) -> Unit,
    openScreen: () -> Unit,
    selected: BookPrintType,
    onPrintTypeChanged: (BookPrintType) -> Unit
) {


    TopAppBar(
        title = {
            Text("Knihovna")
        },

//        navigationIcon = {
//            IconButton(onClick = { /* doSomething() */ }) {
//                Icon(Icons.Filled.Menu, contentDescription = "Menu")
//            }
//        },
        actions = {
            IconButton(onClick = openScreen) {
                Icon(
                    Icons.Default.Add,
                    tint = Color.White,
                    contentDescription = "Create own book"
                )
            }
//            OutlinedButton(
//                modifier = Modifier.padding(end = 4.dp),
//                onClick = {
//                    onPrintTypeChanged(MyBookPrintType.Book)
//                },
//                colors = if (selected == MyBookPrintType.Book) ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.primaryVariant) else ButtonDefaults.outlinedButtonColors()
//            ) {
//                Text(text = "Knihy")
//            }
//            OutlinedButton(
//                modifier = Modifier.padding(end = 4.dp),
//                onClick = {
//                    onPrintTypeChanged(MyBookPrintType.EBook)
//
//                },
//                colors = if (selected == MyBookPrintType.EBook) ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.primaryVariant) else ButtonDefaults.outlinedButtonColors()
//            ) {
//                Text(text = "E-knihy")
//            }
//            OutlinedButton(
//                modifier = Modifier.padding(end = 4.dp),
//                onClick = {
//                    onPrintTypeChanged(MyBookPrintType.AudioBook)
//                },
//                colors = if (selected == MyBookPrintType.AudioBook) ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.primaryVariant) else ButtonDefaults.outlinedButtonColors()
//            ) {
//                Text(text = "Audio")
//            }
        },
    )
}
