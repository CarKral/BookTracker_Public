package com.example.book_tracker.features.feature_search.presentation

import android.app.Activity
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.presentation.components.DotsTyping
import com.example.book_tracker.core.presentation.components.SnackbarManager
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.features.feature_search.presentation.SearchState.UiState
import com.example.book_tracker.features.feature_search.presentation.components.BookItemSearch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    state: SearchState,
    viewModel: SearchScreenViewModel,
    openScreen: (String) -> Unit,
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }

    val activity = LocalContext.current as Activity
    activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    fun hideKeyboard() {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

//    fun clearTextFieldFocus() = focusManager.clearFocus()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (expanded) 150.dp else 56.dp),
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding()
                ) {

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
//        elevation = AppBarDefaults.TopAppBarElevation,
                        color = Color.Transparent
                    ) {
                        SearchTextField(
//                        modifier = Modifier.weight(1f),
                            searchQuery = state.searchQuery ?: "",
                            onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                            searchBooks = { viewModel.searchItems() },
                            hideKeyboard = { hideKeyboard() },
                            expanded = expanded,
                            expandedChanged = { expanded = !expanded }
                        )
                    }

                    this@TopAppBar.AnimatedVisibility(visible = expanded) {
                        Column(modifier = Modifier.padding(top = 56.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(top = 0.dp, bottom = 0.dp, start = 8.dp, end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                val janMelvil = "Jan Melvil Publishing"
                                val selfHelp = "Self-help books"

                                fun searchByChip(value: String) {
                                    viewModel.onSearchQueryChanged(value)
                                    viewModel.searchItems()
                                    hideKeyboard()
                                }

                                MySearchChip(state.searchQuery == janMelvil, janMelvil) { searchByChip(janMelvil) }
                                Spacer(modifier = Modifier.width(8.dp))
                                MySearchChip(state.searchQuery == selfHelp, selfHelp) { searchByChip(selfHelp) }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(top = 0.dp, bottom = 0.dp, start = 8.dp, end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                fun searchByChip() {
                                    // viewModel.onSearchQueryChanged(value)
                                    viewModel.onOrderByNewestCHanged(!state.orderByNewest)
                                    hideKeyboard()
                                }

                                Text(text = "Seřadit od:")
                                Spacer(modifier = Modifier.width(4.dp))
                                MySearchChip(state.orderByNewest, "Nejnovější") { searchByChip() }
                            }
                        }
                    }
                }
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state.uiState) {
                is UiState.Idle -> InitialColumn()
                is UiState.Loading -> ColumnForLoading()
                is UiState.Success -> {
                    if (!state.items.isNullOrEmpty()) {
                        SearchScreenContent(
                            state.items,
                            hideKeyboard = { hideKeyboard() },
                            onItemClick = {
                                openScreen(BookTrackerScreens.BookDetailSearchScreen.route + "?bookId=${it}")
                            },
                            onAddClick = { item, isAdded ->
                                if (isAdded) {
                                    viewModel.addBookToDatabase(
                                        item = item,
                                        onSuccess = { SnackbarManager.showMessage(R.string.successfully_added_to_library) },
                                        onExist = { SnackbarManager.showMessage(R.string.book_already_exist_in_library) }
                                    )
                                }
                            }
                        )
                    } else InitialColumn()
                }
                is UiState.Error -> {
                    state.uiState.throwable?.let {
                        Text(text = "Error: $it", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MySearchChip(selected: Boolean, value: String, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        border = if (selected) BorderStroke(1.dp, MaterialTheme.colors.primary) else null
    ) {
        Text(text = value)
    }
}

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChanged: (String?) -> Unit,
    searchBooks: () -> Unit,
    hideKeyboard: () -> Unit,
    expanded: Boolean,
    expandedChanged: () -> Unit,
) {

//    val interactionSource = remember { MutableInteractionSource() }
//
//    val textStyle = LocalTextStyle.current
//    val textColor = textStyle.color.takeOrElse {
//        MaterialTheme.colors.onSurface
//    }
//    var textFieldValue by remember { mutableStateOf(TextFieldValue(searchQuery, TextRange(searchQuery.length))) }
//    textFieldValue = textFieldValue.copy(text = searchQuery) // make sure to keep the value updated
//    val focusRequester = FocusRequester()
//    SideEffect {
//        focusRequester.requestFocus()
//    }


    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
//            modifier = modifier
//                .padding(top = 8.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),

        //            .fillMaxWidth(),
        //                .background(color = MaterialTheme.colors.primary),

        placeholder = {
            Text(
                modifier = Modifier.alpha(ContentAlpha.medium),
                text = stringResource(R.string.enter_book_or_author),
                color = Color.White
            )
        },
        textStyle = TextStyle(
            color = Color.White,
            fontSize = MaterialTheme.typography.subtitle1.fontSize
        ),
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Filled.Search,
//                    contentDescription = "Search",
//                    tint = Color.White.copy(alpha = ContentAlpha.medium)
//                )
//            },
        trailingIcon = {
            Row() {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchQueryChanged("") },
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear, contentDescription = "Clear",
                            tint = Color.White
                        )
                    }
                }
                IconButton(
                    onClick = {
                        hideKeyboard()
                        searchBooks()
                    },
                    enabled = searchQuery.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search, contentDescription = "Search",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = expandedChanged
                ) {
                    Icon(
                        imageVector = if (!expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Icon for expand options",
                        tint = Color.White
                    )
                }
            }
        },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { hideKeyboard() }
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
        )
    )

}

@Composable
fun InitialColumn() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tak co to dnes bude?",
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "🧐",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun SearchScreenContent(
    books: List<Item>,
    hideKeyboard: () -> Unit,
    onItemClick: (String) -> Unit,
    onAddClick: (Item, Boolean) -> Unit
) {

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (lazyColumnState.isScrollInProgress) hideKeyboard()

        items(books) {
            BookItemSearch(
                item = it,
                onBookItemClick = { onItemClick(it.id ?: "") },
                onAddClick = { item, isAdded -> onAddClick(item, isAdded) }
            )
        }
    }
}

@Composable
fun ColumnForLoading() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DotsTyping()
        Text(
            text = "Načítání...",
            modifier = Modifier.padding(16.dp)
        )
    }
}
