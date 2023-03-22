package com.example.book_tracker.features.feature_auth.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.book_tracker.R
import com.example.book_tracker.core.data.data_source.connection.ConnectivityObserver
import com.example.book_tracker.core.presentation.components.SnackbarManager
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.core.presentation.util.TestTags
import com.example.book_tracker.features.feature_auth.presentation.AuthState.UiState
import timber.log.Timber

@Composable
fun AuthScreen(
    state: AuthState,
    connectionState: ConnectivityObserver.ConnectionState,
    viewModel: AuthScreenViewModel,
    openAndPopUp: (String, String) -> Unit,
) {
    val context = LocalContext.current
    val connectionAvailable = connectionState == ConnectivityObserver.ConnectionState.Available

    val launcher = rememberLauncher(viewModel)
    val scope = rememberCoroutineScope()

//    if (!connectionAvailable)
//        SnackbarManager.showMessage(R.string.internet_connection_not_available)
//    else SnackbarManager.showMessage(R.string.internet_connection_available)

    Timber.i(state.toString())

    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Image(
                modifier = Modifier.testTag(TestTags.IMAGE_BOOK_TAG),
                painter = painterResource(id = R.drawable.books),
                contentDescription = "Kniha"
            )

            when (state.uiState) {
                is UiState.Idle -> {
                    GoogleSignInButton(
                        connectionAvailable = connectionAvailable,
                        signInClicked = {
                            viewModel.signIn(launch = { launcher.launch(it) })
                        }
                    )
                }
                is UiState.Loading -> LinearProgressIndicator()
                is UiState.CreatingUser -> {
                    LinearProgressIndicator()
                    SnackbarManager.showMessage(R.string.creating_user)
                }
                is UiState.Success -> {
                    if (state.uiState.data) {
                        SnackbarManager.showMessage(R.string.successfully_log_in)
                        openAndPopUp(BookTrackerScreens.HomeScreen.route, BookTrackerScreens.AuthScreen.route)
                    }
                }
                is UiState.Error -> {
                    SnackbarManager.showMessage(R.string.generic_error)
                    state.uiState.throwable?.let {
                        Text(text = "Error: $it", textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
fun rememberLauncher(
    viewModel: AuthScreenViewModel,
): ActivityResultLauncher<IntentSenderRequest> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        viewModel.signInWithCredentials(result)
    }
}

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    connectionAvailable: Boolean,
    signInClicked: () -> Unit
) {
    Column(
        modifier = modifier
//            .testTag(TestTags.SIGN_IN_BUTTON)
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!connectionAvailable) {
            Text(
                modifier = modifier.padding(16.dp),
                text = "Připojení k internetu není k dispozici. \n\nBez toho se nepřihlásíš.",
                textAlign = TextAlign.Center
            )
        }

        Card(
            modifier = modifier
                .testTag(TestTags.SIGN_IN_BUTTON)
                .alpha(if (connectionAvailable) 1f else 0.5f)
                .clickable(
                    enabled = connectionAvailable
                ) {
                    signInClicked()
                },
            shape = RoundedCornerShape(5.dp),
            elevation = 8.dp,
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 24.dp)
                        .size(32.dp),
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "google_logo"
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.sign_in_with_google),
                    style = MaterialTheme.typography.button,
                )
            }
        }
    }
}