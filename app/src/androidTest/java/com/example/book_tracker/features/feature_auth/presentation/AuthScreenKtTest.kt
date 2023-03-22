package com.example.book_tracker.features.feature_auth.presentation

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsRule
import com.example.book_tracker.core.data.data_source.connection.NetworkConnectivityObserver
import com.example.book_tracker.core.di.AppModule
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.example.book_tracker.core.presentation.util.TestTags
import com.example.book_tracker.features.feature_main.presentation.MainActivity
import com.example.book_tracker.ui.theme.BookTrackerTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class)
class AuthScreenKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val intentTestRule = IntentsRule()


    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.activity.setContent {
            val navController = rememberNavController()
            BookTrackerTheme {
                NavHost(navController = navController, startDestination = BookTrackerScreens.AuthScreen.route) {
                    composable(route = BookTrackerScreens.AuthScreen.route) {
                        val viewModel = hiltViewModel<AuthScreenViewModel>()
                        val state by viewModel.state.collectAsState()

                        val connectionState by networkConnectivityObserver.observe()
                            .collectAsState(initial = networkConnectivityObserver.currentConnectionState)

                        AuthScreen(
                            state = state,
                            connectionState = connectionState,
                            viewModel = viewModel,
                            openAndPopUp = { route, popUp -> }
                        )
                    }
                }
            }
        }
    }

    @Test
    fun image_isVisible() {
        composeRule.onNodeWithTag(TestTags.IMAGE_BOOK_TAG).assertExists()
    }

    @Test
    fun signInButton_isVisible() {
        composeRule.onNodeWithTag(TestTags.SIGN_IN_BUTTON).assertExists()
    }

    @Test
    fun networkConnection_isUnavailable() {

    }

    @Test
    fun signInIntent_isLaunched() {
        composeRule.onNodeWithTag(TestTags.SIGN_IN_BUTTON).performClick()

        val intent = ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST
        val expectedIntent: Matcher<Intent> = allOf(
            hasAction(intent),)

        val data = Intent(ActivityResultContracts.StartIntentSenderForResult.EXTRA_INTENT_SENDER_REQUEST)
        intending(expectedIntent).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, data))

    }
}