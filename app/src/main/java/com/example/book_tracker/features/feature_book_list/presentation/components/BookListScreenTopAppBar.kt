package com.example.book_tracker.features.feature_book_list.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BookListScreenTopAppBar(
    bookShelfString: String?,
    popUp: () -> Unit,
    openScreen: (String) -> Unit
) {

    TopAppBar(
        title = {
            Text("Polička - $bookShelfString")
        },

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
            // RowScope here, so these icons will be placed horizontally
//
//            IconButton(onClick = {
//                openScreen(BookTrackerScreens.ProfileScreen.route)
//            }) {
//                Icon(Icons.Filled.Settings, contentDescription = "Settings")
//            }
        },
    )
}