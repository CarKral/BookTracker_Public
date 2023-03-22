package com.example.book_tracker.features.feature_book.presentation.book_detail_json

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BookDetailJsonScreen(
    state: BookDetailJsonState,
    viewModel: BookDetailJsonScreenViewModel,
    popUp: () -> Unit
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getBook()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detail knihy RAW",
//                style = MaterialTheme.typography.h5
                    )
                },

                navigationIcon = {
                    IconButton(onClick = popUp) {
                        Icon(Icons.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = "Back")
                    }
                },
                actions = {

                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            state.item?.let {
                SelectableText(
                    text = "Access Info\n" + it.accessInfo.toString().replace(",", ",\n")
                )
                SelectableText(text = "Etag\n" + it.etag.toString())
                SelectableText(text = "ID\n" + it.id.toString())
                SelectableText(text = "kind\n" + it.kind.toString())
//                SelectableText(text = "Sale Info\n" + it.saleInfo.toString().replace(",", ",\n"))
                SelectableText(
                    text = "Search Info\n" + it.searchInfo.toString().replace(",", ",\n")
                )
                SelectableText(text = "Self Link\n" + it.selfLink.toString().replace(",", ",\n"))
                SelectableText(
                    text = "Volume Info\n" + it.volumeInfo.toString().replace(",", ",\n")
                )
            }
        }
    }
}

@Composable
fun SelectableText(text: String) {
    SelectionContainer {
        Text(text = text)
    }
}
