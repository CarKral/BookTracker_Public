package com.example.book_tracker.features.feature_book.presentation.book_edit

import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.work.WorkInfo
import coil.compose.AsyncImage
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookPrintType
import com.example.book_tracker.core.presentation.components.LoadingRowWithText
import com.example.book_tracker.core.presentation.components.SnackbarManager
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.BOOK_ID
import com.example.book_tracker.core.presentation.util.*
import com.example.book_tracker.features.feature_book.presentation.book_edit.BookEditState.*
import java.time.LocalDateTime


enum class DatePickerKind {
    ADDED, STARTED, FINISHED, PUBLISHED;
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BookEditScreen(
    state: BookEditState,
    viewModel: BookEditScreenViewModel,
    popUp: () -> Unit,
    popUpLastTwo: () -> Unit,
    openScreen: (String) -> Unit,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showResetReadingDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getBook()
    }

    viewModel.getWorkInfoList().observeAsState().apply {
        viewModel.setWorkInfoList(this.value)
    }

    val imageScaleWorkInfo = remember(key1 = state.outputWorkInfoList) {
        if (state.outputWorkInfoList?.isNotEmpty() == true) state.outputWorkInfoList[0]
        else null
    }


    /** Launcher for gallery/content result */
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            it?.let {
                viewModel.applyImageScale(it)

                when (imageScaleWorkInfo?.state) {
                    WorkInfo.State.RUNNING -> {
                        viewModel.onSavingChanged(Status.SAVING)
                        println("Work is RUNNING")
                    }
                    WorkInfo.State.SUCCEEDED -> {
//                        println("Work is SUCCEEDED")
                        val outputImageUri =
                            imageScaleWorkInfo.outputData.getString(Constants.KEY_IMAGE_URI)
                        if (!outputImageUri.isNullOrEmpty()) {
                            println("Work is SUCCEEDED - outputUri = $outputImageUri")
                        }
                    }
                    WorkInfo.State.FAILED -> {
                        println("Work is FAILED")
                    }
                    WorkInfo.State.CANCELLED -> {
                        println("Work is CANCELLED")
                    }
                    WorkInfo.State.ENQUEUED -> {
                        println("Work is ENQUEUED")
                    }
                    WorkInfo.State.BLOCKED -> {
                        println("Work is BLOCKED")
                    }
                    else -> {}
                }
            }
        }
    )

    fun hideKeyboard() {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    Scaffold(
        topBar = {
            BookDetailEditTopAppBar(
                state.isBookNew,
                state.isEdited,
                popUp,
                openDetail = {
                    openScreen(BookTrackerScreens.BookDetailSearchScreen.route + "?$BOOK_ID=${state.book.googleBookId}")
                },
                saveBook = {
                    hideKeyboard()
                    if (state.isBookNew) {
                        viewModel.addNewBookToLibrary(onSuccess = {
                            SnackbarManager.showMessage(R.string.book_create_success)
                            viewModel.cancelWork()
                            popUp()
                        })
                    } else {
                        viewModel.saveBookToDatabase(onSuccess = {
                            SnackbarManager.showMessage(R.string.book_update_success)
                            viewModel.cancelWork()
                            popUp()
                        })
                    }
                }
            )
        },

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!state.isBookNew) {
                if (state.status == Status.LOADING) LoadingRowWithText(text = stringResource(R.string.loading))
                else {
                    if (state.status == Status.SAVING) CircularProgressIndicator(
                        modifier = Modifier.padding(
                            4.dp
                        )
                    )
                    BookDetailEditScreenContent(
                        book = state.book,
                        imageUri = state.imageUri,
                        onMyBookChanged = { myUpdatedBook -> viewModel.onMyBookChanged(myUpdatedBook) },
                        onDeleteBook = { showDeleteDialog = true },
                        hideKeyboard = { hideKeyboard() },
                        openGallery = { launcher.launch("image/*") }
                    )
                }
            } else if (state.isBookNew) {
//                Text(text = "NEW")
                if (state.status == Status.SAVING) CircularProgressIndicator(
                    modifier = Modifier.padding(
                        4.dp
                    )
                )
                BookDetailEditScreenContent(
                    book = state.book,
                    imageUri = state.imageUri,
                    onMyBookChanged = { myUpdatedBook -> viewModel.onMyBookChanged(myUpdatedBook) },
                    onDeleteBook = { showDeleteDialog = true },
                    hideKeyboard = { hideKeyboard() },
                    openGallery = { launcher.launch("image/*") }
                )
            } else {
                LoadingRowWithText(text = stringResource(R.string.nothing_to_see))
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                modifier = Modifier
                    .padding(32.dp),
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = "DEJ BACHA! 😱") },
                text = { Text(text = "Opravdu si přeješ smazat tuto knihu a veškerou tvojí aktivitu u ní? \nTato akce nelze vrátit.") },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(text = "NÉÉÉÉÉÉ 😢")
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteMyBook(
                            onSuccess = {
                                popUpLastTwo()
                                SnackbarManager.showMessage(R.string.book_delete_success)
                            })
                    }) {
                        Text(text = "ANO PROSÍM 🤓")
                    }
                }
            )
        }
    }
}

@Composable
fun BookDetailEditTopAppBar(
    isNewBook: Boolean,
    isEdited: Boolean,
    popUp: () -> Unit,
    openDetail: () -> Unit,
    saveBook: () -> Unit,
) {
    TopAppBar(
        title = { Text(if (isNewBook) "Tvorba vlastní knihy" else "Úprava údajů o knize") },
        navigationIcon = {
            IconButton(
                onClick = popUp
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "back",
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier.padding(start = 8.dp),
                onClick = saveBook,
                enabled = isEdited,
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_round_done_24),
                    tint = if (isEdited) Color.White else Color.Gray,
                    contentDescription = "Done"
                )
            }
        },
    )
}

@Composable
fun BookDetailEditScreenContent(
    modifier: Modifier = Modifier,
    book: Book,
    imageUri: Uri?,
    onMyBookChanged: (book: Book) -> Unit,
    onDeleteBook: () -> Unit,
    hideKeyboard: () -> Unit,
    openGallery: () -> Unit
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var currentDatePicker by remember { mutableStateOf(DatePickerKind.ADDED) }
    var currentDateValue by remember { mutableStateOf(LocalDateTime.now()) }

    /** Přiřadí datum pro daný typ */
    fun datePickerWanted(kind: DatePickerKind) {
        hideKeyboard()
        currentDatePicker = kind
        currentDateValue = when (kind) {
            DatePickerKind.ADDED -> book.addedDate?.let { localDateTimeFromTimestamp(it) }
            DatePickerKind.STARTED -> book.startedReading?.let { localDateTimeFromTimestamp(it) }
            DatePickerKind.FINISHED -> book.finishedReading?.let { localDateTimeFromTimestamp(it) }
            else -> LocalDateTime.now()
        }
        showDatePicker = true
    }

    if (showDatePicker) {
        MyDatePickerDialog(
            date = currentDateValue ?: LocalDateTime.now(),
            onDateSet = { newDate ->
                when (currentDatePicker) {
                    DatePickerKind.ADDED -> onMyBookChanged(
                        book.copy(
                            addedDate = timestampFromLocalDateTime(
                                newDate
                            )
                        )
                    )
                    DatePickerKind.STARTED -> onMyBookChanged(
                        book.copy(
                            startedReading = timestampFromLocalDateTime(
                                newDate
                            )
                        )
                    )
                    DatePickerKind.FINISHED -> onMyBookChanged(
                        book.copy(
                            finishedReading = timestampFromLocalDateTime(
                                newDate
                            )
                        )
                    )
                    DatePickerKind.PUBLISHED -> {}
                }
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = modifier
                .padding(16.dp)
        ) {
            Surface(

                modifier = modifier
                    .shadow(elevation = 16.dp, clip = true)
                    .clip(MaterialTheme.shapes.small)
                    .clickable {
                        openGallery()
                    },
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        modifier = modifier.height(200.dp),
                        placeholder = painterResource(R.drawable.book_cover_placeholder),
//                        model = myBook.photoUrl,
                        model = imageUri,
                        contentScale = ContentScale.FillHeight,
                        contentDescription = "Book Cover Image",
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.book_cover_placeholder),
                        contentDescription = "Book Cover Image",
                        modifier = modifier.height(200.dp),
                        contentScale = ContentScale.FillHeight,
                    )
                }
            }

            Icon(
                modifier = modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(20.dp),
                imageVector = Icons.Default.Edit, contentDescription = "Pick new thumbnail"
            )

        }

        Divider(thickness = 1.dp, modifier = modifier.padding(vertical = 8.dp, horizontal = 4.dp))

        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val printType = book.printType?.toMutableList() ?: mutableListOf()
            val printTypeValues = BookPrintType.values()


            for (printTypeValue in printTypeValues) {
                val type = printTypeValue.toString()
                val isChecked = printType.contains(type)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Text(text = printTypeValue.getLabel(context))
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {

                            if (isChecked) printType.remove(type)
                            else printType.add(type)

                            onMyBookChanged(book.copy(printType = printType))
                        },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = Color.LightGray
                        )


                    )
                }
            }
        }

        MyTextField(
            value = book.note,
            label = stringResource(id = R.string.public_note_section),
            onValueChange = { onMyBookChanged(book.copy(note = it)) })

        MyTextField(
            value = book.borrowedFrom,
            label = stringResource(R.string.borrowed_from_label),
            onValueChange = { onMyBookChanged(book.copy(borrowedFrom = it)) })

        MyTextField(
            value = book.borrowedTo,
            label = stringResource(R.string.borrowed_to_label),
            onValueChange = { onMyBookChanged(book.copy(borrowedTo = it)) })

        MyReadOnlyTextFieldWithDatePicker(
            value = localDateTimeStringFromTimestamp(book.addedDate),
            label = stringResource(R.string.added_date_label),
            onValueChange = {},
            onClick = {
                datePickerWanted(DatePickerKind.ADDED)
            })

        MyReadOnlyTextFieldWithDatePicker(
            value = localDateTimeStringFromTimestamp(book.startedReading),
            label = stringResource(R.string.started_date_label),
            onValueChange = {

            },
            onClick = {
                datePickerWanted(DatePickerKind.STARTED)
            })

        MyReadOnlyTextFieldWithDatePicker(
            value = localDateTimeStringFromTimestamp(book.finishedReading),
            label = stringResource(R.string.finished_date_label),
            onValueChange = {
            },
            onClick = {
                datePickerWanted(DatePickerKind.FINISHED)
            })

        Divider(thickness = 1.dp, modifier = modifier.padding(vertical = 8.dp, horizontal = 4.dp))

        MyTextField(
            value = book.title,
            label = stringResource(R.string.title_label),
            onValueChange = { onMyBookChanged(book.copy(title = it)) })

        MyTextField(
            value = book.subtitle,
            label = stringResource(R.string.subtitle_label),
            onValueChange = { onMyBookChanged(book.copy(subtitle = it)) })

        MyTextField(
            value = book.authors,
            label = stringResource(R.string.author_label),
            onValueChange = { onMyBookChanged(book.copy(authors = it)) })

        MyTextField(
            value = book.publisher,
            label = stringResource(R.string.publisher_label),
            onValueChange = { onMyBookChanged(book.copy(publisher = it)) })
//
        MyTextField(
            value = book.publishedDate,
            label = stringResource(R.string.published_date_label),
            onValueChange = { onMyBookChanged(book.copy(publishedDate = it)) })

        MyTextField(
            value = book.categories,
            label = stringResource(R.string.categories_label),
            onValueChange = { onMyBookChanged(book.copy(categories = it)) })

//            MyTextField(
//                value = MyBookPrintType.valueOf(printType).getLabel(LocalContext.current),
//                label = "Typ knihy",
//                onValueChange = {
//
//            })

//
//
//            MyTextField(
//                value = myBook.rating,
//                label = "Moje hodnocení (0-5)",
//                onValueChange = {
////                    if (it.isDigitsOnly() && it.isNotEmpty() && it.count() <= 5) {
////                        onMyBookChanged(myBook.copy(pageCount = it.toInt()))
////                    } else if (it.isEmpty()) {
////                        onMyBookChanged(myBook.copy(pageCount = null))
////                    }
//                    if (it.isDigitsOnly()) onMyBookChanged(myBook.copy(rating = it.toDouble()))
//                    else if (it.isEmpty()) onMyBookChanged(myBook.copy(rating = null))
//                },
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Number
//                )
//            )


        MyTextField(
            value = book.pageCount,
            label = stringResource(id = R.string.page_count),
            onValueChange = {
                if (it.isDigitsOnly() && it.isNotEmpty() && it.count() <= 5) {
                    onMyBookChanged(book.copy(pageCount = it.toInt()))
                } else if (it.isEmpty()) {
                    onMyBookChanged(book.copy(pageCount = null))
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        MyTextField(
            value = book.currentPage?.floorDoubleToInt(),
            label = stringResource(R.string.current_page_label),
            onValueChange = { value ->
                if (value.count() <= 5) {
                    val number = value.replaceCommaToDot()
                    if (number.isEmpty()) onMyBookChanged(book.copy(currentPage = null))
                    else if (number.toDoubleOrNull() != null) {
                        onMyBookChanged(book.copy(currentPage = number.toDouble()))
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

//            MyTextField(
//                value = book.readingSpeed,
//                label = "Rychlost čtení",
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Done
//                ),
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        hideKeyboard()
//                    }),
//                onValueChange = {
//                    if (it.isDigitsOnly() && it.isNotEmpty() && it.count() <= 5) {
//                        onMyBookChanged(book.copy(readingSpeed = it.toInt()))
//                    } else if (it.isEmpty()) {
//                        onMyBookChanged(book.copy(readingSpeed = null))
//                    }
//                }
//            )

    }

    Divider(thickness = 2.dp, modifier = modifier.padding(horizontal = 4.dp))

    Spacer(modifier = modifier.height(100.dp))

    Text(
        modifier = modifier.padding(8.dp),
        text = "Book ID:   ${book.id}"
    )

    Divider(thickness = 0.5.dp)

    DeleteTextButton(onDelete = onDeleteBook)
}


@Composable
fun MyDatePickerDialog(
    date: LocalDateTime,
    onDateSet: (date: LocalDateTime) -> Unit,
    onDismiss: () -> Unit,
) {

    val datePickerDialog = DatePickerDialog(LocalContext.current)
    datePickerDialog.updateDate(date.year, date.monthValue - 1, date.dayOfMonth)
//    datePickerDialog.datePicker.se
    datePickerDialog.setOnDateSetListener { datePicker, year, month, day ->

        onDateSet(LocalDateTime.of(year, month + 1, day, 0, 0))
        onDismiss()
    }
    datePickerDialog.setOnDismissListener {
        onDismiss()
    }
    datePickerDialog.show()

//    val picker = MaterialDatePicker.Builder.datePicker().build()
//    val activity = LocalContext.current as Activity
//    picker.show(activity.fragmentManager, picker.toString())


}

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: Any?,
    label: String?,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit
) {

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        value = value?.toString() ?: "",
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(text = label ?: "") },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}


//**/
@Composable
fun MyReadOnlyTextFieldWithDatePicker(
    modifier: Modifier = Modifier,
    value: String?,
    label: String?,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
) {
    Box {
        TextField(
            value = value ?: "",
            enabled = false,
            onValueChange = onValueChange,
            label = { Text(text = label ?: "") },
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 16.dp),
            colors = TextFieldDefaults.textFieldColors(disabledTextColor = MaterialTheme.colors.onSurface)
        )
        Box(modifier = Modifier
            .matchParentSize()
            .alpha(0f)
            .clickable {
                onClick()
            })
    }
}

@Composable
private fun DeleteTextButton(onDelete: () -> Unit) {
    TextButton(
        contentPadding = PaddingValues(),
        onClick = onDelete,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(R.string.delete_book_label),
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.error
        )
    }
}
