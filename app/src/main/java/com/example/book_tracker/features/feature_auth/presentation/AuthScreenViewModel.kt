package com.example.book_tracker.features.feature_auth.presentation

import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.use_case.auth.SignInWithCredentialsUseCase
import com.example.book_tracker.core.domain.use_case.user.UserUseCases
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.features.feature_auth.presentation.AuthState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val signInWithCredentialsUseCase: SignInWithCredentialsUseCase,
    private val userUseCases: UserUseCases,
    private val authRepository: AuthRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = AuthState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun signIn(launch: (IntentSenderRequest) -> Unit) {
        _state.update { it.copy(uiState = UiState.Loading) }

        viewModelScope.launch(dispatchers.io) {
            try {
                var intent = authRepository.beginSignInResult().pendingIntent

                launch(IntentSenderRequest.Builder(intent).build())
            } catch (e: Exception) {
                _state.update { it.copy(uiState = UiState.Error(e)) }
            }
        }
    }

    fun signInWithCredentials(activityResult: ActivityResult) {
        viewModelScope.launch(dispatchers.io) {
            signInWithCredentialsUseCase(activityResult = activityResult).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _state.update { it.copy(uiState = UiState.Loading) }
                    is Resource.Success -> {
                        _state.update { it.copy(firebaseUser = resource.data, uiState = UiState.Success(true)) }
                        getUser()
                    }
                    is Resource.Error -> _state.update { it.copy(uiState = UiState.Error(resource.exception)) }
                }
            }
        }
    }

    private fun getUser() {
        Timber.i("GET USER uid: ${_state.value.firebaseUser?.uid.toString()}")

        _state.value.firebaseUser?.uid?.let { id ->
            viewModelScope.launch(dispatchers.io) {
                userUseCases.getUserUseCase(id).collect { resource ->

                    Timber.i("GET USER : ${resource.toString()}")
                    _state.update {
                        when (resource) {
                            is Resource.Loading ->
                                it.copy(uiState = UiState.Loading)
                            is Resource.Success -> {
                                if (resource.data == null) {
                                    createUser()
                                    it.copy(myUser = null, uiState = UiState.Success(false))
                                } else {
                                    it.copy(myUser = resource.data, uiState = UiState.Success(true))
                                }
                            }

                            is Resource.Error ->
                                it.copy(uiState = UiState.Error(resource.exception))
                        }
                    }
                }
            }
        }
    }

    private fun createUser() {
        viewModelScope.launch(dispatchers.io) {
            userUseCases.addUserUseCase(_state.value.firebaseUser).collect { resource ->
                _state.update {
                    when (resource) {
                        is Resource.Loading -> it.copy(uiState = UiState.CreatingUser)
                        is Resource.Success -> it.copy(myUser = resource.data, uiState = UiState.Success(true))
                        is Resource.Error -> it.copy(uiState = UiState.Error(resource.exception))
                    }
                }
            }
        }
    }


}