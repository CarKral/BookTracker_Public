package com.example.book_tracker.features.feature_profile.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.book_tracker.R
import com.example.book_tracker.core.presentation.components.SnackbarManager

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    viewModel: ProfileScreenViewModel,
    rebirthApp: () -> Unit,
    restartApp: (String) -> Unit,
    popUp: () -> Unit,
//    darkTheme: Boolean,
//    onThemeChanged: (darkTheme: Boolean) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current


    fun hideKeyboard() {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    LaunchedEffect(Unit) {
        viewModel.getUserInfo()
        viewModel.getMyUserInfo()
    }


    Scaffold(
        topBar = {
            ProfileScreenTopAppBar(viewModel, rebirthApp, restartApp, popUp)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            state.user?.let {


                Row(
                    modifier = Modifier,
//                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    MyTextField(

                        modifier = Modifier.weight(1f),
                        value = state.myUser?.name, label = "Tvé jméno",
                        onValueChange = { viewModel.onMyUserChanged(state.myUser?.copy(name = it)) })
                    IconButton(
                        modifier = Modifier.padding(start = 8.dp),
                        onClick = {
                            hideKeyboard()
                            viewModel.saveUserToDatabase(onSuccess = {
                                popUp()
                                SnackbarManager.showMessage(R.string.user_update_success)
                            })
                        }
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_round_done_24),
                            tint = MaterialTheme.colors.onSurface,
                            contentDescription = "Done"
                        )
                    }
                }

                Text(text = "ProviderID: " + state.user.providerId)
                Text(text = "UID: " + state.user.uid.toString())
                Text(text = "Email: " + state.user.email.toString())
                Text(text = "PhotoUrl: " + state.user.photoUrl.toString())
                AsyncImage(
                    placeholder = painterResource(R.drawable.book_cover_placeholder),
                    model = state.user.photoUrl,
//                                modifier = Modifier.height(300.dp),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = "Book Cover Image",
                )
            }
        }
    }
}

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: Any?,
    label: String?,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit
) {

    TextField(
        value = value?.toString() ?: "",
        onValueChange = {
            onValueChange(it)
        },
        maxLines = 1,
        label = { Text(text = label ?: "") },
        modifier = modifier
            .fillMaxWidth(),
//            .padding(vertical = 4.dp, horizontal = 16.dp),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun ProfileScreenTopAppBar(
    viewModel: ProfileScreenViewModel,
    rebirthApp: () -> Unit,
    restartApp: (String) -> Unit,
    popUp: () -> Unit,
) {

    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(
                "Profil",
//                style = MaterialTheme.typography.h5
            )
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

            IconButton(onClick = {
                viewModel.onSignOutClick(onSuccess = {
                    Toast.makeText(context, R.string.successfully_log_out, Toast.LENGTH_SHORT).show()
                    rebirthApp()
                })
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    tint = Color.White,
                    contentDescription = "Sign out"
                )
            }
        }
    )
}
