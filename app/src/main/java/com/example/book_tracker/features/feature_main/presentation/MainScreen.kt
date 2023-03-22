package com.example.book_tracker.features.feature_main.presentation

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.book_tracker.BookTrackerAppState
import com.example.book_tracker.R
import com.example.book_tracker.core.presentation.components.SnackbarManager
import com.example.book_tracker.core.presentation.navigation.BookTrackerNavGraph
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.core.presentation.util.TestTags
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen(
    state: MainState,
    viewModel: MainViewModel,
) {

    val appState = rememberBookTrackerAppState()
    val navController = appState.navController

    val bottomBarRoutes = listOf(
        BookTrackerScreens.HomeScreen.route,
        BookTrackerScreens.SearchScreen.route,
        BookTrackerScreens.LibraryScreen.route,
        BookTrackerScreens.BookDetailScreen.route,
        BookTrackerScreens.BookDetailSearchScreen.route,
        BookTrackerScreens.UserListScreen.route,
        )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    viewModel.onShowBottomBarChange(navBackStackEntry?.destination?.route in bottomBarRoutes)

    Scaffold(
        scaffoldState = appState.scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.padding(8.dp).testTag(TestTags.SNACKBAR_HOST),
                snackbar = { snackBarData ->
                    Snackbar(
                        snackbarData = snackBarData,
                        backgroundColor = MaterialTheme.colors.surface,
                        contentColor = MaterialTheme.colors.onSurface,
                        actionColor = MaterialTheme.colors.primary,
                    )
                })
        },
        bottomBar = {
            if (state.showBottomBar) BottomNavigationBar(
                currentRoute = currentRoute,
                navigateTo = { route -> appState.bottomBarNavigate(route) },
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            BookTrackerNavGraph(
                startDestination = state.startDestination,
                appState = appState)
        }
    }
}

@Composable
fun BottomNavigationBar(
    modifier : Modifier = Modifier,
    currentRoute: String?,
    navigateTo: (String) -> Unit,
) {
    val items = listOf(
        BottomNavigationItem.Home,
        BottomNavigationItem.People,
        BottomNavigationItem.Search,
        BottomNavigationItem.Library,
    )

    BottomNavigation(
        modifier = modifier.testTag(TestTags.BOTTOM_NAVIGATION_BAR)

    ) {
        items.forEachIndexed { index, item ->
            val title = LocalContext.current.getString(item.titleResource)
            val selected = currentRoute == item.route

            BottomNavigationItem(
                modifier = modifier.testTag(TestTags.BOTTOM_NAVIGATION_ITEM),
                selected = selected,
                onClick = { navigateTo(item.route) },
                icon = {
                    Icon(
                        painterResource(id = item.iconResource),
                        contentDescription = title
                    )
                },
                label = { Text(text = title) },
                alwaysShowLabel = true,
            )
        }
    }
}

sealed class BottomNavigationItem(
    var titleResource: Int,
    var iconResource: Int,
    var route: String
) {
    object Home :
        BottomNavigationItem(
            R.string.home,
            R.drawable.ic_baseline_home_24,
            BookTrackerScreens.HomeScreen.route
        )

    object People :
        BottomNavigationItem(
            R.string.people,
            R.drawable.ic_baseline_people_24,
            BookTrackerScreens.UserListScreen.route
        )

    object Search :
        BottomNavigationItem(
            R.string.search,
            R.drawable.ic_baseline_search_24,
            BookTrackerScreens.SearchScreen.route
        )

    object Library :
        BottomNavigationItem(
            R.string.library,
            R.drawable.ic_baseline_library_books_24,
            BookTrackerScreens.LibraryScreen.route
        )

}

@Composable
fun rememberBookTrackerAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackBarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): BookTrackerAppState {

//    val networkConnectivity = NetworkConnectivityObserver(LocalContext.current)
//    val connectionState by networkConnectivityObserver.observe()
//        .collectAsState(initial = networkConnectivityObserver.currentConnectionState)

    return remember(scaffoldState, navController, coroutineScope) {
        BookTrackerAppState(
            scaffoldState,
            navController,
            snackBarManager,
            resources,
            context,
            coroutineScope
        )
    }
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
