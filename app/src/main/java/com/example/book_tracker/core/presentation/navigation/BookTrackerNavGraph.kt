package com.example.book_tracker.core.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.book_tracker.BookTrackerAppState
import com.example.book_tracker.core.data.data_source.connection.NetworkConnectivityObserver
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.BOOKSHELF
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.BOOKSHELF_ARGUMENT
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.BOOK_ID
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.BOOK_ID_ARGUMENT
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.USER_ID
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.USER_ID_ARGUMENT
import com.example.book_tracker.features.feature_auth.presentation.AuthScreen
import com.example.book_tracker.features.feature_auth.presentation.AuthScreenViewModel
import com.example.book_tracker.features.feature_book.presentation.book_detail.BookDetailScreen
import com.example.book_tracker.features.feature_book.presentation.book_detail.BookDetailScreenViewModel
import com.example.book_tracker.features.feature_book.presentation.book_detail_json.BookDetailJsonScreen
import com.example.book_tracker.features.feature_book.presentation.book_detail_json.BookDetailJsonScreenViewModel
import com.example.book_tracker.features.feature_book.presentation.book_detail_search.BookDetailSearchScreen
import com.example.book_tracker.features.feature_book.presentation.book_detail_search.BookDetailSearchScreenViewModel
import com.example.book_tracker.features.feature_book.presentation.book_edit.BookEditScreen
import com.example.book_tracker.features.feature_book.presentation.book_edit.BookEditScreenViewModel
import com.example.book_tracker.features.feature_book.presentation.user_book_detail.UserBookDetailScreen
import com.example.book_tracker.features.feature_book.presentation.user_book_detail.UserBookDetailScreenViewModel
import com.example.book_tracker.features.feature_book_list.presentation.my_book_list.BookListScreen
import com.example.book_tracker.features.feature_book_list.presentation.my_book_list.BookListScreenViewModel
import com.example.book_tracker.features.feature_book_list.presentation.user_book_list.UserBookListScreen
import com.example.book_tracker.features.feature_book_list.presentation.user_book_list.UserBookListScreenViewModel
import com.example.book_tracker.features.feature_home.presentation.HomeScreen
import com.example.book_tracker.features.feature_home.presentation.HomeScreenViewModel
import com.example.book_tracker.features.feature_library.presentation.my_library.LibraryScreen
import com.example.book_tracker.features.feature_library.presentation.my_library.LibraryScreenViewModel
import com.example.book_tracker.features.feature_library.presentation.user_library.UserLibraryScreen
import com.example.book_tracker.features.feature_library.presentation.user_library.UserLibraryScreenViewModel
import com.example.book_tracker.features.feature_note_list.presentation.BookNoteListScreen
import com.example.book_tracker.features.feature_note_list.presentation.BookNoteListScreenViewModel
import com.example.book_tracker.features.feature_profile.presentation.create_profile.CreateProfileScreen
import com.example.book_tracker.features.feature_profile.presentation.profile.ProfileScreen
import com.example.book_tracker.features.feature_profile.presentation.profile.ProfileScreenViewModel
import com.example.book_tracker.features.feature_reading_list.presentation.BookReadingListScreen
import com.example.book_tracker.features.feature_reading_list.presentation.BookReadingListScreenViewModel
import com.example.book_tracker.features.feature_search.presentation.SearchScreen
import com.example.book_tracker.features.feature_search.presentation.SearchScreenViewModel
import com.example.book_tracker.features.feature_settings.presentation.SettingsScreen
import com.example.book_tracker.features.feature_settings.presentation.SettingsScreenViewModel
import com.example.book_tracker.features.feature_user_list.presentation.UserListScreen
import com.example.book_tracker.features.feature_user_list.presentation.UserListScreenViewModel
import com.example.book_tracker.features.feature_welcome.presentation.WelcomeScreen
import com.example.book_tracker.features.feature_welcome.presentation.WelcomeScreenViewModel


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BookTrackerNavGraph(
//    navController: NavHostController,
    startDestination: String,
    appState: BookTrackerAppState
) {

    NavHost(
        navController = appState.navController,
        startDestination = startDestination,

        ) {

        composable(route = BookTrackerScreens.WelcomeScreen.route) {
            val viewModel = hiltViewModel<WelcomeScreenViewModel>()

            WelcomeScreen(
                viewModel = viewModel,
                openAndPopUp = { route, popUp ->
                    appState.navigateAndPopUp(
                        route, popUp
                    )
                })
        }

        composable(route = BookTrackerScreens.AuthScreen.route) {
            val viewModel = hiltViewModel<AuthScreenViewModel>()
            val state by viewModel.state.collectAsState()

            val networkConnectivity = NetworkConnectivityObserver(LocalContext.current)
            val connectionState by networkConnectivity.observe()
                .collectAsState(initial = networkConnectivity.currentConnectionState)

            AuthScreen(
                state = state,
                connectionState = connectionState,
                viewModel = viewModel,
                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            )
        }

        composable(route = BookTrackerScreens.CreateProfileScreen.route) {
            CreateProfileScreen()
        }

        composable(route = BookTrackerScreens.HomeScreen.route) {

            val viewModel = hiltViewModel<HomeScreenViewModel>()
            val state by viewModel.state.collectAsState()

            HomeScreen(
                state = state,
                viewModel = viewModel,
                openScreen = { route -> appState.navigate(route) }
            )
        }

        composable(
            route = BookTrackerScreens.SearchScreen.route,
        ) {
            val viewModel = hiltViewModel<SearchScreenViewModel>()
            val state by viewModel.state.collectAsState()

            SearchScreen(
                state = state,
                viewModel = viewModel,
                openScreen = { route -> appState.navigate(route) }
            )
        }

        composable(route = BookTrackerScreens.LibraryScreen.route) {
            val viewModel = hiltViewModel<LibraryScreenViewModel>()
            val state by viewModel.state.collectAsState()

            LibraryScreen(
                state = state,
                viewModel = viewModel,
                openScreen = { route -> appState.navigate(route) }
            )
        }

        composable(
//            // Adding optional arguments:
//            // They must be included using query parameter syntax ("?argName={argName}")
            route = BookTrackerScreens.BookDetailScreen.route + BOOK_ID_ARGUMENT,
            arguments = listOf(navArgument(BOOK_ID) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {

            val viewModel = hiltViewModel<BookDetailScreenViewModel>()
            viewModel.onBookIdChanged(it.arguments?.getString(BOOK_ID, null))
            val state by viewModel.state.collectAsState()
//            println(state.toString())

            BookDetailScreen(
                state = state,
                viewModel = viewModel,
                popUp = { appState.popUp() },
                openScreen = { route -> appState.navigate(route) }
            )
        }

        composable(
//            // Adding optional arguments:
//            // They must be included using query parameter syntax ("?argName={argName}")
            route = BookTrackerScreens.BookDetailJsonScreen.route + BOOK_ID_ARGUMENT,
            arguments = listOf(navArgument(BOOK_ID) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            val viewModel = hiltViewModel<BookDetailJsonScreenViewModel>()
            viewModel.onBookIdChanged(it.arguments?.getString(BOOK_ID, null))

            val state by viewModel.state.collectAsState()

            BookDetailJsonScreen(
                state = state,
                viewModel = viewModel,
                popUp = { appState.popUp() },
            )
        }
        composable(
            route = BookTrackerScreens.BookListScreen.route + BOOKSHELF_ARGUMENT,
            arguments = listOf(navArgument(BOOKSHELF) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            val viewModel = hiltViewModel<BookListScreenViewModel>()
            viewModel.onBookShelfChanged(it.arguments?.getString(BOOKSHELF, null))

            val state by viewModel.state.collectAsState()

            BookListScreen(
                state = state,
                viewModel = viewModel,
                popUp = { appState.popUp() },
                openScreen = { route -> appState.navigate(route) }
            )
        }

        composable(
            route = BookTrackerScreens.BookEditScreen.route + BOOK_ID_ARGUMENT,
            arguments = listOf(navArgument(BOOK_ID) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            val bookId = it.arguments?.getString(BOOK_ID, null)
            val viewModel = hiltViewModel<BookEditScreenViewModel>()
            viewModel.onBookIdChanged(bookId)
            viewModel.onIsBookNewChanged(bookId == "NEW")

            val state by viewModel.state.collectAsState()


            BookEditScreen(
                state = state,
                viewModel = viewModel,
                popUp = { appState.popUp() },
                popUpLastTwo = { appState.popUpLastTwo() },
                openScreen = { route -> appState.navigate(route) }
            )
        }
        composable(
            route = BookTrackerScreens.BookDetailSearchScreen.route + BOOK_ID_ARGUMENT,
            arguments = listOf(
                navArgument(BOOK_ID) {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                })
        ) {
            val viewModel = hiltViewModel<BookDetailSearchScreenViewModel>()
            viewModel.onBookIdChanged(it.arguments?.getString(BOOK_ID, null))

            val state by viewModel.state.collectAsState()

            BookDetailSearchScreen(
                state = state,
                viewModel = viewModel,
                popUp = { appState.popUp() },
                openScreen = { route -> appState.navigate(route) }
            )
        }

        composable(
            route = BookTrackerScreens.BookNoteListScreen.route + BOOK_ID_ARGUMENT,
            arguments = listOf(navArgument(BOOK_ID) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            val viewModel = hiltViewModel<BookNoteListScreenViewModel>()
            viewModel.onBookIdChanged(it.arguments?.getString(BOOK_ID, null))

            val state by viewModel.state.collectAsState()

            BookNoteListScreen(
                state = state,
                viewModel = viewModel,
                popUp = { appState.popUp() },
                openScreen = { route -> appState.navigate(route) }
            )
        }


        composable(
            route = BookTrackerScreens.BookReadingListScreen.route + BOOK_ID_ARGUMENT,
            arguments = listOf(navArgument(BOOK_ID) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            val viewModel = hiltViewModel<BookReadingListScreenViewModel>()
            viewModel.onBookIdChanged(it.arguments?.getString(BOOK_ID, null))

            val state by viewModel.state.collectAsState()

            BookReadingListScreen(
                state = state,
                viewModel = viewModel,
                popUp = { appState.popUp() },
                openScreen = { route -> appState.navigate(route) }
            )
        }


        composable(route = BookTrackerScreens.ProfileScreen.route) {
            val viewModel = hiltViewModel<ProfileScreenViewModel>()
            val state by viewModel.state.collectAsState()

            ProfileScreen(
                state = state,
                viewModel = viewModel,
                rebirthApp = { appState.triggerRebirth() },
                restartApp = { route -> appState.clearAndNavigate(route) },
                popUp = { appState.popUp() },
//                darkTheme = appState.darkTheme,
//                onThemeChanged = { appState.themeChanged() }
            )
        }
        composable(route = BookTrackerScreens.UserListScreen.route) {
            val viewModel = hiltViewModel<UserListScreenViewModel>()
            val state by viewModel.state.collectAsState()

            UserListScreen(
                state = state,
                viewModel = viewModel,
                openScreen = { route -> appState.navigate(route) },
                popUp = { appState.popUp() }
            )
        }


        composable(
//            // Adding optional arguments:
//            // They must be included using query parameter syntax ("?argName={argName}")
            route = BookTrackerScreens.UserLibraryScreen.route + USER_ID_ARGUMENT,
            arguments = listOf(navArgument(USER_ID) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            val viewModel = hiltViewModel<UserLibraryScreenViewModel>()
            viewModel.onUserIdChanged(it.arguments?.getString(USER_ID, null))

            val state by viewModel.state.collectAsState()

            UserLibraryScreen(
                state = state,
                viewModel = viewModel,
                popUp = { appState.popUp() },
                openScreen = { route -> appState.navigate(route) }
            )
        }


        composable(
//            // Adding optional arguments:
//            // They must be included using query parameter syntax ("?argName={argName}")
            route = BookTrackerScreens.UserBookDetailScreen.route + USER_ID_ARGUMENT + "/" + BOOK_ID_ARGUMENT,
            arguments = listOf(navArgument(USER_ID) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }, navArgument(BOOK_ID) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            val viewModel = hiltViewModel<UserBookDetailScreenViewModel>()
            viewModel.onUserIdChanged(it.arguments?.getString(USER_ID, null))
            viewModel.onBookIdChanged(it.arguments?.getString(BOOK_ID, null))
            val state by viewModel.state.collectAsState()

            UserBookDetailScreen(
                state = state,
                viewModel = viewModel,
                popUp = { appState.popUp() },
                openScreen = { route -> appState.navigate(route) }
            )
        }

        composable(
            route = BookTrackerScreens.UserBookListScreen.route + USER_ID_ARGUMENT + "/" + BOOKSHELF_ARGUMENT,
            arguments = listOf(navArgument(USER_ID) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }, navArgument(BOOKSHELF) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) {
            val viewModel = hiltViewModel<UserBookListScreenViewModel>()
            viewModel.onUserIdChanged(it.arguments?.getString(USER_ID, null))
            viewModel.onBookShelfChanged(it.arguments?.getString(BOOKSHELF, null))
            val state by viewModel.state.collectAsState()

            UserBookListScreen(
                state = state,
                viewModel = viewModel,
                popUp = { appState.popUp() },
                openScreen = { route -> appState.navigate(route) }
            )
        }

        composable(route = BookTrackerScreens.SettingsScreen.route) {
            val viewModel = hiltViewModel<SettingsScreenViewModel>()
            val state by viewModel.state.collectAsState()

            SettingsScreen(
                state = state,
                viewModel = viewModel,
                openScreen = { route -> appState.navigate(route) },
                popUp = { appState.popUp() }
            )
        }

//        composable(
//            // Adding optional arguments:
//            // They must be included using query parameter syntax ("?argName={argName}")
//            BookTrackerScreens.BookDetailScreen.route + "?bookId={bookId}",
//            arguments = listOf(navArgument("bookId") {
//                type = NavType.StringType
//                defaultValue = null
//                nullable = true
//            }),
//
//            ) {
//            val viewModel = hiltViewModel<BookDetailScreenViewModel>()
//
//            BookDetailScreen(
//                navController = navController,
//                viewModel = viewModel,
//                id = it.arguments?.getString("noteId", null)
//            )
//
//        }
    }
}