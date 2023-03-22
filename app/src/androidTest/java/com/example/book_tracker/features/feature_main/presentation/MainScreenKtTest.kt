package com.example.book_tracker.features.feature_main.presentation

import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.book_tracker.R
import com.example.book_tracker.core.di.AppModule
import com.example.book_tracker.core.presentation.util.TestTags
import com.example.book_tracker.ui.theme.BookTrackerTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class MainScreenKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()


    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            BookTrackerTheme {
                val viewModel = hiltViewModel<MainViewModel>()
                val state by viewModel.state.collectAsState()

                MainScreen(
                    state = state,
                    viewModel = viewModel
                )
            }
        }
    }

    @Test
    fun bottomNavigationBar_isVisible() {
        composeRule.onNodeWithTag(TestTags.BOTTOM_NAVIGATION_BAR).assertExists()
    }

    @Test
    fun snackbarHost_exists() {
    }

    @Test
    fun bottomNavigationBarItems_isClickable() {
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.home)).assertExists().performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.people)).assertExists().performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.search)).assertExists().performClick()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.library)).assertExists().performClick()
    }





}