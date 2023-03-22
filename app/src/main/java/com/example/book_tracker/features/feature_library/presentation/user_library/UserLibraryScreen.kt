package com.example.book_tracker.features.feature_library.presentation.user_library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.model.user.MyUser
import com.example.book_tracker.core.presentation.components.LoadingRowWithText
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.BOOKSHELF
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.BOOK_ID
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens.Companion.USER_ID
import com.example.book_tracker.features.feature_library.presentation.components.BookShelfSection
import com.example.book_tracker.features.feature_library.presentation.user_library.UserLibraryState.UiState


@Composable
fun UserLibraryScreen(
    state: UserLibraryState,
    viewModel: UserLibraryScreenViewModel,
    openScreen: (String) -> Unit,
    popUp: () -> Unit

) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.getBooksByBookShelf()
        viewModel.getUserInfo()
    }
    Scaffold(
        topBar = { UserDetailScreenTopAppBar(state.myUser, openScreen, popUp) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
        ) {

            when (state.uiState) {
                is UiState.Loading -> LoadingRowWithText(text = stringResource(R.string.loading))
                is UiState.Success -> {
                    for (map in state.libraryBooks) {
                        val bookShelf = map.key
                        val books = map.value

                        if (books.isNotEmpty()) {
                            BookShelfSection(
                                BookShelf.valueOf(bookShelf).getLabel(LocalContext.current),
                                books,
                                onMoreBookClick = {
                                    openScreen(
                                        BookTrackerScreens.UserBookListScreen.route
                                                + "?$USER_ID=${state.myUser?.id}/" + "/"
                                                + "?$BOOKSHELF=${bookShelf}"
                                    )
                                },
                                onBookItemClick = {
                                    openScreen(
                                        BookTrackerScreens.UserBookDetailScreen.route
                                                + "?$USER_ID=${state.myUser?.id}" + "/"
                                                + "?$BOOK_ID=${it}"
                                    )
                                })
                        }
                    }
                }
                is UiState.Error -> {
                    Text(text = "Error: ${state.uiState.throwable}")
                }
            }
//            for (bookShelf in bookShelves) {
//                val books =
//                    viewModel.getBooksByBookShelf(bookShelf)
//                        ?.collectAsState(emptyList())?.value?.sortedByDescending { it.addedDate }
//                        ?: emptyList()
//
//                if (books.isNotEmpty()) BookShelfSection(
//                    bookShelf, books,
//                    onMoreBookClick = { openScreen(BookTrackerScreens.UserBookListScreen.route + "?userId=${state.myUser?.id}/" + "?bookShelf=${bookShelf}") },
//                    onBookItemClick = {
//                        openScreen(BookTrackerScreens.UserBookDetailScreen.route + "?userId=${state.myUser?.id}/" + "?bookId=${it}")
//                    })
//            }
        }
    }
}

@Composable
fun UserDetailScreenTopAppBar(
    myUser: MyUser?,
//    viewModel: LibraryScreenViewModel,
//    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    popUp: () -> Unit
) {

    TopAppBar(
        title = {
            Text(
                text = "Knihovna - ${myUser?.name}",
                maxLines = 1
            )

        },
        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            // RowScope here, so these icons will be placed horizontally
//            IconButton(onClick = {
//            }) {
//                Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
//            }
//            IconButton(onClick = {
////                openScreen(BookTrackerScreens.SearchScreen.route)
//            }) {
//                Icon(Icons.Filled.Search, contentDescription = "Search")
//            }
//            IconButton(onClick = {
////                openScreen(BookTrackerScreens.ProfileScreen.route)
//            }) {
//                Icon(Icons.Filled.Person, contentDescription = "Profile")
//            }
//            IconButton(onClick = { viewModel.onSignOutClick(restartApp) }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_logout),
//                    contentDescription = "Sign out"
//                )
//            }
        },
    )
}
//
//@Composable
//fun BookShelfSection(
//    openScreen: (String) -> Unit,
//    bookShelf: BookShelf,
//    myUser: MyUser?,
//    books: List<MyBook>,
//    onMoreClick: () -> Unit,
//    onBookItemClick: (String?) -> Unit
//) {
//    val bookShelfString = bookShelf.getLabel(LocalContext.current)
//
////    Timber.i(books.toString())
//
//    Column(
//        verticalArrangement = Arrangement.SpaceBetween,
//        horizontalAlignment = Alignment.Start
//    ) {
//        Row(
//            verticalAlignment = Alignment.Top,
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//
//
//            Text(
//                modifier = Modifier.padding(start = 8.dp),
//                text = "$bookShelfString:",
//                style = MaterialTheme.typography.subtitle1.copy(fontSize = 18.sp, fontWeight = FontWeight.Normal, letterSpacing = 2.sp)
//            )
//            Spacer(modifier = Modifier.weight(1f))
//
//            TextButton(
//                modifier = Modifier.padding(end = 4.dp),
//                onClick = onMoreClick,
////                enabled = false,
////                enabled = books.isNotEmpty()
////                modifier = Modifier.clip(RoundedCornerShape(50.dp))
//            ) {
//                Text(text = stringResource(R.string.give_me_more))
//            }
//        }
//
//        BooksList(openScreen, bookShelf, myUser, books, onBookItemClick)
//
//        Divider(
//            thickness = 2.dp,
//            modifier = Modifier.padding(top = 16.dp, start = 4.dp, end = 4.dp),
//        )
//    }
//}
//
//
//@Composable
//fun BooksList(
//    openScreen: (String) -> Unit,
//    bookShelf: BookShelf,
//    myUser: MyUser?,
//    books: List<MyBook>,
//    onBookItemClick: (String?) -> Unit
//) {
//
//    LazyRow(
//        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 16.dp),
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        items(books) {
//            BookItemLibrary(
//                myBook = it,
//                onBookItemClick = { onBookItemClick(it.id) },
//
//                )
//        }
//    }
//}