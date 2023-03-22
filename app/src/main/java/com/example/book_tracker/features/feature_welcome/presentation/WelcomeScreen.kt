package com.example.book_tracker.features.feature_welcome.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    viewModel: WelcomeScreenViewModel,
    openAndPopUp: (String, String) -> Unit
) {
    LaunchedEffect(true) {
        delay(500L)

        viewModel.saveWelcomeState(true)
        openAndPopUp(BookTrackerScreens.HomeScreen.route, BookTrackerScreens.WelcomeScreen.route)

    }
}