package com.example.book_tracker.features.feature_main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.repository.WelcomeStateDataStoreRepository
import com.example.book_tracker.core.domain.use_case.auth.AuthUseCases
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.core.presentation.navigation.BookTrackerScreens
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainState(
    val startDestination: String = BookTrackerScreens.WelcomeScreen.route,
    val user: FirebaseUser? = null,
    val isLoading: Boolean = true,
    val showBottomBar: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepository: WelcomeStateDataStoreRepository,
    private val authUseCases: AuthUseCases,
    private val dispatchers: DispatcherProvider
//    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun onShowBottomBarChange(showBottomBar: Boolean) {
        _state.update { it.copy(showBottomBar = showBottomBar) }
    }

    private fun onStartDestinationChange(startDestination: String) {
        _state.update { it.copy(startDestination = startDestination) }
    }

    private fun onUserChange(user: FirebaseUser?) {
        _state.update { it.copy(user = user) }
    }

    private fun onIsLoadingChange(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    init {
        getAuthSession()
    }

    /** Get current user from authRepository and decide if it should go to start destination (HomeScreen or WelcomeScreen)
     * or directly to AuthScreen to offer to user to sign in. */
    private fun getAuthSession() {
        viewModelScope.launch(dispatchers.io) {
            authUseCases.getAuthUserUseCase().collect() {

                when (it) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        onUserChange(it.data)
                        if (_state.value.user != null) getStartDestination()
                        else onStartDestinationChange(BookTrackerScreens.AuthScreen.route)

                    }
                    is Resource.Error -> {

                    }
                }

            }
        }
    }

    /** Checks if user was already on WelcomeScreen and decide if it should go to HomeScreen or WelcomeScreen. */
    private fun getStartDestination() {
        viewModelScope.launch(dispatchers.io) {
            dataStoreRepository.getWelcomeState().collect { completed ->
                onShowBottomBarChange(completed)
                onStartDestinationChange(
                    if (completed) BookTrackerScreens.HomeScreen.route
                    else BookTrackerScreens.WelcomeScreen.route
                )
            }
            onIsLoadingChange(false)
        }
    }
}

