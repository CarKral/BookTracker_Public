package com.example.book_tracker

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.example.book_tracker.core.presentation.components.SnackbarManager
import com.example.book_tracker.core.presentation.components.SnackbarMessage.Companion.toMessage
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Stable
class BookTrackerAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    private val context: Context,
    coroutineScope: CoroutineScope,
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                scaffoldState.snackbarHostState.showSnackbar(
                    message = text,
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short,
                )
            }
        }
    }

    /** Function for navigating to given route from NavGraph (launchSingleTop = true) */
    fun navigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    /** Function for pop controller's back stack  */
    fun popUp() {
        navController.popBackStack()

    }

    /** Function for navigating to given route (launchSingleTop = true) and popUpTo given route (inclusive = true) from NavGraph */
    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) {
                inclusive = true
            }
        }
    }

    fun bottomBarNavigate(route: String,) {
        navController.navigate(route) {
            navController.graph.startDestinationRoute?.let {
                popUpTo(it) { saveState = true }
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    /** Po smazání knihy přejde z BookEditScreen, přes BookDetailScreen až na předcházející screen - to může být buďto HomsScreen, LibraryScreen, nebo BookListScreen, proto nemůže mít nastaveno staticky */
    fun popUpLastTwo() {
        // Předpředposlední
        val thirdLast = navController.backQueue[navController.backQueue.size.minus(3)].destination.route
        // Předposlední
        val secondLast = navController.backQueue[navController.backQueue.size.minus(2)].destination.route

        thirdLast?.let { it ->
            navController.navigate(it) {
                launchSingleTop = true
                secondLast?.let { itt ->
                    popUpTo(itt) {
                        inclusive = true
                    }
                }
            }
        }
    }

    /** Function for navigating to given route from NavGraph (launchSingleTop = true) and popUpTo(0) => clears other screens in back stack */
    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) {
                inclusive = true
            }
        }
    }

    fun triggerRebirth() {
        Toast.makeText(context, R.string.rebirthing_app, Toast.LENGTH_SHORT).show()
        ProcessPhoenix.triggerRebirth(context)
    }
}

