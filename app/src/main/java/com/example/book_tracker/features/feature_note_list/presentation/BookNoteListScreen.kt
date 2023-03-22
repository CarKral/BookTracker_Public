package com.example.book_tracker.features.feature_note_list.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.BookNote
import com.example.book_tracker.core.presentation.components.Loading
import com.example.book_tracker.core.presentation.components.SnackbarManager
import com.example.book_tracker.features.feature_note_list.presentation.BookNoteListState.UiState
import com.example.book_tracker.features.feature_note_list.presentation.components.NoteItem


@Composable
fun BookNoteListScreen(
    state: BookNoteListState,
    viewModel: BookNoteListScreenViewModel,
    popUp: () -> Unit,
    openScreen: (String) -> Unit,
) {

    val context = LocalContext.current
    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.getNotes()
    }

    Scaffold(
        topBar = {
            BookNoteListScreenTopAppBar(popUp, openScreen,
                addNote = {
                    viewModel.addNote(
                        onSuccess = {
                            SnackbarManager.showMessage(R.string.random_note_success)
                        })
                })
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            when (state.uiState) {
            is UiState.Loading -> Loading()
            is UiState.Success -> {
                if (state.notes != null) {
                    BookNoteListScreenContent(
                        viewModel = viewModel,
                        notes = state.notes,
                        lazyColumnState = lazyColumnState,
                        onNoteItemClick = {
//                openScreen(BookTrackerScreens.BookDetailScreen.route + "?bookId=${it}")
                        })
                } else {
                    Text(text = "K tété knize nejsou žádné poznámky.")
                }
            }
            is UiState.Error -> { Text(text = "Error: ${state.uiState.throwable}") }
        }

        }

    }
}

@Composable
fun BookNoteListScreenTopAppBar(
//    viewModel: LibraryScreenViewModel,
//    restartApp: (String) -> Unit,
    popUp: () -> Unit,
    openScreen: (String) -> Unit,
    addNote: () -> Unit
) {

    TopAppBar(
        title = {
            Text("Poznámky ke knize")
        },

        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(Icons.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Back")
            }
        },
        actions = {
            // RowScope here, so these icons will be placed horizontally

            IconButton(onClick = addNote) {
                Icon(Icons.Filled.Add,
                    tint = Color.White,
                    contentDescription = "Add note")
            }
        },
    )
}

@Composable
fun BookNoteListScreenContent(
    viewModel: BookNoteListScreenViewModel,
    notes: List<BookNote>,
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    onNoteItemClick: (String?) -> Unit
//    navController: NavController
) {
    NoteList(
        viewModel = viewModel,
        notes = notes,
        lazyColumnState = lazyColumnState,
        onNoteItemClick = onNoteItemClick
    )
}

@Composable
fun NoteList(
    viewModel: BookNoteListScreenViewModel,
    notes: List<BookNote>,
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    onNoteItemClick: (String?) -> Unit
) {

    LazyColumn(
        state = lazyColumnState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(notes) {
            NoteItem(note = it, onNoteItemClick = { })
        }
//        items(books) {
//            BookItemHome(
//                book = it,
//                onBookItemClick = { onBookItemClick(it.id) }
//            )
//        }
    }
}

