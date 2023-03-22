package com.example.book_tracker.features.feature_book.presentation.book_detail

import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.book_tracker.core.di.AppModule
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.fakeBook
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.core.presentation.util.TestTags
import com.example.book_tracker.features.feature_main.presentation.MainActivity
import com.example.book_tracker.ui.theme.BookTrackerTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class BookDetailScreenKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    lateinit var book: Book
    private var state: BookDetailState = BookDetailState()

    @Before
    fun setUp() {
        book = fakeBook
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            BookTrackerTheme {
                NavHost(navController = navController, startDestination = BookTrackerScreens.BookDetailScreen.route) {
                    composable(route = BookTrackerScreens.BookDetailScreen.route) {
                        val viewModel = hiltViewModel<BookDetailScreenViewModel>()
                        state = viewModel.state.collectAsState().value


                        viewModel.onBookChanged(book)

                        BookDetailScreen(
                            state = state,
                            viewModel = viewModel,
                            popUp = {},
                            openScreen = { route -> }
                        )
                    }
                }
            }
        }
    }

    @Test
    fun topAppBar_isVisible() {
        composeRule.onNodeWithTag(TestTags.TOP_APP_BAR).assertExists()
    }
    @Test
    fun iconButtonEdit_exists_isClickable() {
        composeRule.onNodeWithTag(TestTags.ICON_BUTTON_EDIT).assertExists()
        composeRule.onNodeWithTag(TestTags.ICON_BUTTON_EDIT).performClick()
    }
    @Test
    fun iconButtonDetail_exists_isClickable() {
        composeRule.onNodeWithTag(TestTags.ICON_BUTTON_DETAIL).assertExists()
        composeRule.onNodeWithTag(TestTags.ICON_BUTTON_DETAIL).performClick()
    }

    @Test
    fun book_isLoaded() {
        composeRule.waitUntil(timeoutMillis = 3000) { state.uiState is BookDetailState.UiState.Success }
        composeRule.onNodeWithText(book.title!!).assertExists()
        composeRule.onNodeWithText(book.subtitle!!).assertExists()
        composeRule.onNodeWithText(book.publisher!!).assertExists()
    }
}