package com.example.book_tracker.features.feature_profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.user.MyUser
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.repository.UserRepository
import com.example.book_tracker.core.domain.use_case.auth.AuthUseCases
import com.example.book_tracker.core.domain.use_case.user.UserUseCases
import com.example.book_tracker.core.domain.util.DefaultDispatchers
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.features.feature_profile.presentation.profile.ProfileState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = ProfileState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    init {
        getMyUserInfo()
    }

    fun onMyUserChanged(myNewUser: MyUser?) {
        _state.update { it.copy(myUser = myNewUser) }
    }

    fun getUserInfo() {
        viewModelScope.launch(dispatchers.io) {
            authUseCases.getAuthUserUseCase().collect { resource ->

                when (resource) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _state.update { it.copy(user = resource.data) }
                    }
                    is Resource.Error -> {}
                }
            }
        }
    }

    fun getMyUserInfo() {
        state.value.user?.uid?.let {
            viewModelScope.launch(dispatchers.io) {
                userUseCases.getUserUseCase(it).collect { result ->
                    _state.update {
                        when (result) {
                            is Resource.Loading -> it.copy(uiState = UiState.Loading)
                            is Resource.Success -> it.copy(
                                uiState = UiState.Success(result.data),
                                myUser = result.data
                            )
                            is Resource.Error -> it.copy(uiState = UiState.Error(result.exception))
                        }
                    }
                }
            }
        }
    }

    fun saveUserToDatabase(onSuccess: () -> Unit) {
        viewModelScope.launch(dispatchers.io) {
            userRepository.updateUser(_state.value.myUser, onSuccess = {
                onSuccess()
            })

        }
    }

    fun onSignOutClick(onSuccess: () -> Unit) {
        viewModelScope.launch(dispatchers.io) {
            authRepository.signOut(onSuccess = onSuccess)
        }
    }
}