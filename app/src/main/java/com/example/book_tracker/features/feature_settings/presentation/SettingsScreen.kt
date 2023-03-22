package com.example.book_tracker.features.feature_settings.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.book_tracker.R
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens

@Composable
fun SettingsScreen(
    state: SettingsState,
    viewModel: SettingsScreenViewModel,
    openScreen: (String) -> Unit,
    popUp: () -> Unit,
) {



    Scaffold(
        topBar = { SettingsScreenTopAppBar(openScreen, popUp) },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
//            SettingsScreenContent()

        }
    }
}

@Composable
fun SettingsScreenTopAppBar(
    openScreen: (String) -> Unit,
    popUp: () -> Unit
) {

    TopAppBar(
        title = {
            Text("Nastavení")
        },

        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = {
                openScreen(BookTrackerScreens.ProfileScreen.route)
            }) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_account_circle_24),
                    tint = Color.White,
                    contentDescription = "Profile"
                )
            }
        },
    )
}

@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {


    }
}

