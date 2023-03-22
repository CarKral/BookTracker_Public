package com.example.book_tracker.features.feature_library.presentation.user_library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.use_case.book.GetBookListUseCase
import com.example.book_tracker.features.feature_library.presentation.user_library.UserLibraryState.UiState
import com.example.book_tracker.core.domain.use_case.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLibraryScreenViewModel @Inject constructor(
    private val getBookListUseCase: GetBookListUseCase,
    private val getUserUseCase: GetUserUseCase,
//    private val authRepository: AuthRepository,
//    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserLibraryState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = UserLibraryState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onUserIdChanged(id: String?) {
        _state.update { it.copy(userId = id) }
    }

    fun getBooksByBookShelf() {
        for (bookShelf in BookShelf.values()) {
            _state.value.userId?.let {
                viewModelScope.launch {
                    getBookListUseCase.invoke(it, bookShelf.toString(), 2).collect { result ->
                        _state.update {
                            when (result) {
                                is Resource.Loading -> it.copy(uiState = UiState.Loading)
                                is Resource.Success -> {
                                    val libraryBooksMap = it.libraryBooks
                                    libraryBooksMap[bookShelf.toString()] =
                                        result.data.sortedByDescending { book -> book.addedDate }
                                    it.copy(uiState = UiState.Success, libraryBooks = libraryBooksMap)
                                }
                                is Resource.Error -> it.copy(uiState = UiState.Error(result.exception))
                            }
                        }
                    }
                }
            }
        }
    }

    fun getUserInfo() {
        _state.value.userId?.let {
            viewModelScope.launch {
                getUserUseCase.invoke(it).collect { result ->
                    if (result is Resource.Success) _state.update { it.copy(myUser = result.data) }
                }
            }
        }
    }


}