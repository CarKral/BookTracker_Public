package com.example.book_tracker.features.feature_user_list.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.book_tracker.core.domain.model.user.MyUser
import com.example.book_tracker.core.presentation.components.Loading
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.USER_ID
import com.example.book_tracker.features.feature_user_list.presentation.UserListState.UiState
import com.example.book_tracker.features.feature_user_list.presentation.components.UserList
import com.example.book_tracker.features.feature_user_list.presentation.components.UserListItem


@Composable
fun UserListScreen(
    state: UserListState,
    viewModel: UserListScreenViewModel,
    openScreen: (String) -> Unit,
    popUp: () -> Unit,
) {

//    openScreen(BookTrackerScreens.BookDetailScreen.route)

    val lazyColumnState = rememberLazyListState()
//    val myId = viewModel.getMyFirebaseId()

    Scaffold(
        topBar = { UsersScreenTopAppBar(openScreen, popUp) },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            when (state.uiState) {
                is UiState.Loading ->  Loading()
                is UiState.Success -> {
                    if (state.users != null) {
                        UsersScreenContent(
                            users = state.users,
//                        id = myId,
                            lazyColumnState = lazyColumnState,
                            onUserItemClick = {
                                openScreen(BookTrackerScreens.UserLibraryScreen.route + "?$USER_ID=${it}")
                            })
                    } else {
                        Text(text = "Tady nikdo neníííí.")
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
fun UsersScreenTopAppBar(
//    viewModel: LibraryScreenViewModel,
//    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    popUp: () -> Unit
) {

    TopAppBar(
        title = {
            Text("Čtenáři")
        },

        actions = {
            // RowScope here, so these icons will be placed horizontally
//
//            IconButton(onClick = {
//                openScreen(BookTrackerScreens.ProfileScreen.route)
//            }) {
//                Icon(Icons.Filled.Settings,
//                    tint = Color.White,
//                    contentDescription = "Settings")
//            }
        },
    )
}

@Composable
fun UsersScreenContent(
    users: List<MyUser>,
//    id: String,
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState,
    onUserItemClick: (String?) -> Unit
//    navController: NavController
) {
    UserList(
        users = users,
        lazyColumnState = lazyColumnState,
    ) {
        UserListItem(
            user = it,
            onUserItemClick = { onUserItemClick(it.id) }
        )
    }
}



