package com.example.book_tracker.features.feature_book.presentation.user_book_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.presentation.components.LoadingRowWithText
import com.example.book_tracker.core.presentation.components.SnackbarManager
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.BOOK_ID
import com.example.book_tracker.features.feature_book.presentation._components.*
import com.example.book_tracker.features.feature_book.presentation.user_book_detail.UserBookDetailState.UiState

@Composable
fun UserBookDetailScreen(
    state: UserBookDetailState,
    viewModel: UserBookDetailScreenViewModel,
    popUp: () -> Unit,
    openScreen: (String) -> Unit,
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getBook()
    }

    Scaffold(
        topBar = {
            BookDetailTopAppBar(
                state, popUp,
                openDetail = {
                    openScreen(BookTrackerScreens.BookDetailSearchScreen.route + "?$BOOK_ID=${state.book?.googleBookId}")
                },
                onAddBook = {
                    viewModel.addBookToLibrary(
                        onSuccess = { book ->
//                            viewModel.onMyBookChanged(book)
                            viewModel.onIsAddedChanged(true)

                            SnackbarManager.showMessage(R.string.successfully_added_to_library)

                        },
                        onExist = { exist ->
                            if (exist) SnackbarManager.showMessage(R.string.book_already_exist_in_library)
                        }
                    )
                }
            )
        },
//        containerColor = MaterialTheme.colorScheme.primaryContainer

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (state.uiState) {
                is UiState.Loading -> LoadingRowWithText(text = stringResource(R.string.loading))
                is UiState.Success -> {
                    if (state.book != null) {
                        UserBookDetailScreenContent(
                            book = state.book,
                            openScreen = openScreen,
                        )
                    } else {
                        LoadingRowWithText(text = stringResource(R.string.nothing_to_see))
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
fun BookDetailTopAppBar(
    state: UserBookDetailState,
    popUp: () -> Unit,
    openDetail: () -> Unit,
    onAddBook: () -> Unit,
) {
    val hasGoogleBook = state.book?.googleBookId != null

    TopAppBar(
        title = { Text("Detail knihy v knihovně") },
        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(
                    Icons.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "back"
                )
            }
        },
        actions = {
            if (hasGoogleBook) {
                IconButton(onClick = openDetail) {
                    Icon(
                        painterResource(id = R.drawable.ic_baseline_read_more_24),
                        tint = Color.White,
                        contentDescription = "Google Play Books info"
                    )
                }
            } else if (!state.isAddedToLibrary) {
                IconButton(onClick = onAddBook) {
                    Icon(
                        Icons.Filled.Add,
                        tint = Color.White,
                        contentDescription = "Add to library"
                    )
                }
            }
        },
    )
}

@Composable
fun UserBookDetailScreenContent(
    modifier: Modifier = Modifier,
    book: Book,
    openScreen: (String) -> Unit,
) {
    BookHeaderSection(book = book)

    Divider(thickness = 2.dp, modifier = modifier.padding(horizontal = 4.dp))

    book.note?.let {
        BookNoteSection(note = it)
        Divider(thickness = 2.dp, modifier = modifier.padding(horizontal = 4.dp))
    }

    if (book.bookShelf == BookShelf.CurrentRead.toString()) {
        ReadingSection(
            book = book,
            content = {}
        )
        Spacer(modifier = modifier.height(8.dp))
        Divider(thickness = 2.dp, modifier = modifier.padding(horizontal = 8.dp))

    }

    book.bookShelf?.let {
        BookShelfSection(book = book) {
            Text(
                modifier = modifier.padding(vertical = 8.dp),
                text = BookShelf.valueOf(it).getLabel(LocalContext.current),
                style = MaterialTheme.typography.subtitle2,
            )
        }
        Spacer(modifier = modifier.height(8.dp))

        Divider(thickness = 2.dp, modifier = modifier.padding(horizontal = 4.dp))

    }
    BookInfoSection(book = book, isItMe = false)

    Divider(thickness = 0.5.dp)

    Text(
        modifier = modifier.padding(8.dp),
        text = "Book ID:   ${book.id}"
    )

    Divider(thickness = 0.5.dp)

}
