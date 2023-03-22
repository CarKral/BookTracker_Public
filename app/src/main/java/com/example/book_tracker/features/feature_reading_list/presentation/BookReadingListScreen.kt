package com.example.book_tracker.features.feature_reading_list.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.BookReading
import com.example.book_tracker.core.presentation.components.Loading
import com.example.book_tracker.features.feature_reading_list.presentation.BookReadingListState.UiState
import com.example.book_tracker.features.feature_reading_list.presentation.components.ReadingItem


@Composable
fun BookReadingListScreen(
    state: BookReadingListState,
    viewModel: BookReadingListScreenViewModel,
    popUp: () -> Unit,
    openScreen: (String) -> Unit,
) {

    val context = LocalContext.current
    val lazyColumnState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.getReadings()
    }
    Scaffold(
        topBar = {
            BookReadingListScreenTopAppBar(popUp, openScreen)
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            when (state.uiState) {
                is UiState.Loading ->  Loading()
                is UiState.Success -> {
                    if (state.readings != null) {
                        BookReadingListScreenContent(
                            viewModel = viewModel,
                            readings = state.readings,
                            lazyColumnState = lazyColumnState,
                            onReadingItemClick = {
//                openScreen(BookTrackerScreens.BookDetailScreen.route + "?bookId=${it}")
                            })
                    } else {
                        Text(text = "Žádné čtení.")

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
fun BookReadingListScreenTopAppBar(
    popUp: () -> Unit,
    openScreen: (String) -> Unit,
) {

    TopAppBar(
        title = {
            Text("Přehled čtení")
        },

        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(
                    Icons.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Back"
                )
            }
        },
        actions = {

        },
    )
}

@Composable
fun BookReadingListScreenContent(
    viewModel: BookReadingListScreenViewModel,
    readings: List<BookReading>,
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    onReadingItemClick: (String?) -> Unit
//    navController: NavController
) {
    if (readings.isNotEmpty()) {
        ReadingsList(
            viewModel = viewModel,
            readings = readings,
            lazyColumnState = lazyColumnState,
            onReadingItemClick = onReadingItemClick
        )
    } else {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.nothing_to_see),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ReadingsList(
    viewModel: BookReadingListScreenViewModel,
    readings: List<BookReading>,
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    onReadingItemClick: (String?) -> Unit
) {

    LazyColumn(
        state = lazyColumnState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(readings) {
            ReadingItem(reading = it, onReadingItemClick = { })
        }
    }
}

