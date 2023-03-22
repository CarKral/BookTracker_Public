package com.example.book_tracker.features.feature_user_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.use_case.user.GetUserListUseCase
import com.example.book_tracker.core.domain.util.DefaultDispatchers
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.features.feature_user_list.presentation.UserListState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getUserListUseCase: GetUserListUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(UserListState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = UserListState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    init {
        getUsers()
    }


    private fun getUsers() {


        viewModelScope.launch(dispatchers.io) {
            getUserListUseCase.invoke().collect { result ->
                _state.update {
                    when (result) {
                        is Resource.Loading -> it.copy(uiState = UiState.Loading)
                        is Resource.Success -> {
//                            val list = result.data.map { user ->
//                                user.id == currentUserId
//                            }

                            it.copy(
                                uiState = UiState.Success(result.data),
                                users = result.data
                            )
                        }
                        is Resource.Error -> it.copy(uiState = UiState.Error(result.exception))
                    }
                }
            }
        }
    }
}