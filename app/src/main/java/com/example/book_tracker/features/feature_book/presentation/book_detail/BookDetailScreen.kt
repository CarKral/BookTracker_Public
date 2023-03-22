package com.example.book_tracker.features.feature_book.presentation.book_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.model.book.placeholderBook
import com.example.book_tracker.features.feature_book.presentation.util.visibleReadingBookShelf
import com.example.book_tracker.core.presentation.components.*
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.BOOK_ID
import com.example.book_tracker.core.presentation.util.TestTags
import com.example.book_tracker.features.feature_book.presentation._components.*
import com.example.book_tracker.features.feature_book.presentation.book_detail.BookDetailState.UiState


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BookDetailScreen(
    state: BookDetailState,
    viewModel: BookDetailScreenViewModel,
    popUp: () -> Unit,
    openScreen: (String) -> Unit,
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showResetReadingDialog by remember { mutableStateOf(false) }
    var showSetGoalDialog by remember { mutableStateOf(false) }

    val hasGoogleBook = state.book?.googleBookId != null

    LaunchedEffect(Unit) {
        viewModel.getBook()
        viewModel.getReadings()
    }

    Scaffold(
        topBar = {
            BookDetailTopAppBar(
                state.book, hasGoogleBook, popUp,
                openDetail = {
                    if (hasGoogleBook) openScreen(BookTrackerScreens.BookDetailSearchScreen.route + "?$BOOK_ID=${state.book?.googleBookId}")
                    else SnackbarManager.showMessage(R.string.created_by_user)
                },

                openEdit = { openScreen(BookTrackerScreens.BookEditScreen.route + "?$BOOK_ID=${state.book?.id}") },
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
                        BookDetailScreenContent(
                            book = state.book,
                            openScreen = openScreen,
                            addNote = { viewModel.addNote(onSuccess = { SnackbarManager.showMessage(R.string.random_note_success) }) },
                            newPage = state.newPage ?: state.book.currentPage.toString(),
                            onNewPageChanged = { viewModel.onNewPageChanged(it) },
                            onAddNewReading = {
                                viewModel.addNewReading(
                                    bookId = state.bookId,
                                    onSuccess = { SnackbarManager.showMessage(R.string.new_reading_success) })
                            },
                            onResetReading = { showResetReadingDialog = true },
                            onSetReadingGoal = { showSetGoalDialog = true },
                            onShowReadingList = { openScreen(BookTrackerScreens.BookReadingListScreen.route + "?$BOOK_ID=${state.book.id}") },
                            onAddToBookShelfClick = { bookShelf ->
                                viewModel.addToBookShelf(
                                    bookShelf,
                                    onSuccess = {
                                        SnackbarManager.showMessage(
                                            SnackbarMessage.StringSnackbar(
                                                context.getString(R.string.successfully_added_to_bookshelf)
                                                        + " \"${bookShelf.getLabel(context)}\""
                                            ),
                                        )
                                    })
                            },
                        ) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .width(IntrinsicSize.Min)
                            ) {
                                state.readings?.let {
                                    getChartValues(it)?.let { points ->
                                        println("LIST of POINTS: $it")

                                        if (points.isNotEmpty() && points.size != 1) {
//                                            val pages = state.book.pageCount?.toDouble()
//                                            val speed = state.book.goalReadingSpeed

                                            Divider(thickness = 2.dp, modifier = Modifier.padding(horizontal = 4.dp))

                                            LineChart(
                                                book = state.book,
                                                list = points
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        LoadingRowWithText(text = stringResource(R.string.nothing_to_see))
                    }
                }
                is UiState.Error -> {
                    Text(text = "Error: ${state.uiState.throwable}")
                }
            }
        }

        if (showDeleteDialog) {
            DeleteAlertDialog(
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    viewModel.deleteMyBook(
                        onSuccess = {
                            popUp()
                            SnackbarManager.showMessage(R.string.book_delete_success)
                        })
                }
            )
        }
        if (showResetReadingDialog) {
            ResetReadingAlertDialog(
                onDismiss = { showResetReadingDialog = false },
                onConfirm = {
                    viewModel.resetReading(
                        onSuccess = {
                            SnackbarManager.showMessage(R.string.reading_reset_success)
                            showResetReadingDialog = false
                        })
                }
            )
        }
        if (showSetGoalDialog) {
            ReadingGoalDialog(
                book = state.book,
                onDelete = {
                    viewModel.deleteReadingGoal(
                        onSuccess = {
                            SnackbarManager.showMessage(R.string.reading_goal_successfully_deleted)
                            showSetGoalDialog = false
                        })
                },
                onDismiss = { showSetGoalDialog = false },

                onConfirm = { goalSpeed/*, goalFinished, goalFinishedDate*/ ->
                    viewModel.addNewReadingGoal(
                        newBook = state.book?.copy(
                            goalReadingSpeed = goalSpeed,
//                            goalFinished = goalFinished,
//                            goalFinishedDate = goalFinishedDate,
                        ),
                        onSuccess = {
                            SnackbarManager.showMessage(R.string.reading_goal_success)
                            showSetGoalDialog = false
                        })
                })
        }
    }
}

@Composable
fun DeleteAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        onDismissRequest = onDismiss,
        title = { Text(text = "DEJ BACHA! 😱") },
        text = { Text(text = "Opravdu si přeješ smazat tuto knihu a veškerou tvojí aktivitu u ní? \nTato akce nelze vrátit.") },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = stringResource(R.string.no_way)) }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.yes_please))
            }
        }
    )
}

@Composable
fun ResetReadingAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        onDismissRequest = onDismiss,
        title = { Text(text = "DEJ BACHA! 😱") },
        text = { Text(text = "Opravdu si přeješ vynulovat své čtení u této knihy? Přijdeš i o nastavený cíl čtení. \n\nTato akce nelze vrátit.") },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = stringResource(R.string.no_way)) }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.yes_please))
            }
        }
    )
}

@Composable
fun BookDetailTopAppBar(
    book: Book?,
    hasGoogleBook: Boolean?,
    popUp: () -> Unit,
    openDetail: () -> Unit,
    openEdit: () -> Unit,

    ) {

    TopAppBar(
        modifier = Modifier.testTag(TestTags.TOP_APP_BAR),
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
            IconButton(
                modifier = Modifier.testTag(TestTags.ICON_BUTTON_EDIT),
                onClick = openEdit
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_edit_note_24),
                    tint = Color.White,
                    contentDescription = "Edit book screen"
                )
            }
            IconButton(
//                modifier = Modifier.padding(start = 8.dp),
                modifier = Modifier.testTag(TestTags.ICON_BUTTON_DETAIL),
                onClick = openDetail
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_read_more_24),
                    tint = if (hasGoogleBook == true) Color.White else Color.Gray,
                    contentDescription = "Google Play Books info"
                )
            }
        },
    )
}

@Composable
fun BookDetailScreenContent(
    modifier: Modifier = Modifier,
    book: Book,
    newPage: String?,
    openScreen: (String) -> Unit,
    onNewPageChanged: (String?) -> Unit,
    onAddNewReading: () -> Unit,
    onResetReading: () -> Unit,
    onSetReadingGoal: () -> Unit,
    onShowReadingList: () -> Unit,
    addNote: () -> Unit,
    onAddToBookShelfClick: (BookShelf) -> Unit,
    chart: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        BookHeaderSection(book = book)

        chart()

        Divider(thickness = 2.dp, modifier = modifier.padding(horizontal = 4.dp))

        book.note?.let {
            BookNoteSection(note = it)
            Divider(thickness = 2.dp, modifier = modifier.padding(horizontal = 4.dp))
        }

        if (visibleReadingBookShelf(book)) {
            AddPageSection(
                book = book,
                newPage = newPage,
                onNewPageChanged = onNewPageChanged,
                onAddNewReading = onAddNewReading,
            )

            Divider(
                modifier = modifier
                    .height(1.dp)
                    .padding(horizontal = 8.dp)
            )

            ReadingSection(
                book = book,
            ) {
                ReadingSettingSection(
                    book = book,
                    onResetReading = onResetReading,
                    onSetReadingGoal = onSetReadingGoal,
                    onShowReadingList = onShowReadingList
                )
//            myBook.bookShelf?.let {
//                ReadingSettingSection()
//            }
            }
            Spacer(modifier = modifier.height(8.dp))
            Divider(thickness = 2.dp, modifier = modifier.padding(horizontal = 4.dp))
        }

        book.bookShelf?.let {
            BookShelfSection(book = book) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = modifier.weight(1f))
                    Box {
                        ButtonWithDropDownMenu(
                            book = book,
                            onAddToBookShelfClick = onAddToBookShelfClick
                        )
                    }
                }
            }
            Spacer(modifier = modifier.height(8.dp))
            Divider(thickness = 2.dp, modifier = modifier.padding(horizontal = 4.dp))
        }

        NotesSection(book = book, openScreen = openScreen, addNote = addNote)

        Divider(thickness = 2.dp, modifier = modifier.padding(horizontal = 4.dp))

        BookInfoSection(book = book, isItMe = true)

        Divider(thickness = 0.5.dp)

        Text(
            modifier = modifier.padding(8.dp),
            text = "Book ID:   ${book.id}"
        )

        Divider(thickness = 0.5.dp)

    }
}


@Composable
fun NotesSection(
    modifier: Modifier = Modifier,
    book: Book,
    openScreen: (String) -> Unit,
    addNote: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.End
    ) {

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = "Soukromé poznámky ke knize",
                style = MaterialTheme.typography.subtitle1,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = modifier.weight(1f))

            TextButtonWithIcon(
                text = "Přidat poznámku",
                imageVector = Icons.Filled.Add,
                onClick = addNote
            )
        }

        TextButtonWithIcon(
            text = "Přejít na poznámky",
            imageVector = Icons.Filled.ArrowForward,
            onClick = { openScreen(BookTrackerScreens.BookNoteListScreen.route + "?bookId=${book.id}") }
        )
    }
}

@Composable
fun ButtonWithDropDownMenu(
    modifier: Modifier = Modifier,
    book: Book?, onAddToBookShelfClick: (BookShelf) -> Unit
) {
    val context = LocalContext.current
    var bookShelfMenuExpanded by remember { mutableStateOf(false) }

    val myBookBookShelf = book?.bookShelf
    val myBookBookShelfText =
        myBookBookShelf?.let {
            BookShelf.valueOf(myBookBookShelf).getLabel(context)
        } ?: stringResource(id = R.string.add_to)

    TextButtonWithIcon(
        text = myBookBookShelfText,
        imageVector = Icons.Filled.ArrowDropDown,
        onClick = { bookShelfMenuExpanded = true }
    )

    DropdownMenu(
        properties = PopupProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
        expanded = bookShelfMenuExpanded,
        onDismissRequest = { bookShelfMenuExpanded = false }) {
        for (bookShelf in BookShelf.values()) {

            val selected = myBookBookShelf == bookShelf.toString()

            if (!selected) {
                DropdownMenuItem(
                    onClick = {
                        onAddToBookShelfClick(bookShelf)
                        bookShelfMenuExpanded = false
                    },
                ) {
                    Text(text = bookShelf.getLabel(context))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookDetailScreenPreview() {
    BookDetailScreenContent(
        book = placeholderBook,
        newPage = "0.0",
        openScreen = {},
        onNewPageChanged = {},
        onAddNewReading = { /*TODO*/ },
        onResetReading = { /*TODO*/ },
        onSetReadingGoal = { /*TODO*/ },
        onShowReadingList = { /*TODO*/ },
        addNote = { /*TODO*/ },
        onAddToBookShelfClick = {},
        chart = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteAlertDialogPreview() {
    DeleteAlertDialog(onDismiss = { }, onConfirm = {})
}

@Preview(showBackground = true)
@Composable
fun ResetReadingAlertDialogPreview() {
    ResetReadingAlertDialog(onDismiss = { }, onConfirm = {})
}

@Preview(showBackground = true)
@Composable
fun ResetReadingGoalDialogPreview() {
    ReadingGoalDialog(book = placeholderBook, onDismiss = { }, onConfirm = {}, onDelete = {})
}