package com.example.book_tracker.features.feature_book_list.presentation.user_book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.use_case.book.GetBookListUseCase
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.features.feature_book_list.presentation.user_book_list.UserBookListState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserBookListScreenViewModel @Inject constructor(
    private val getBookListUseCase: GetBookListUseCase,
    private val dispatchers: DispatcherProvider

    ) : ViewModel() {

    private val _state = MutableStateFlow(UserBookListState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = UserBookListState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onUserIdChanged(id: String?) {
        _state.update { it.copy(userId = id) }
    }

    fun onBookShelfChanged(id: String?) {
        _state.update { it.copy(bookShelf = id) }
    }

    fun getBooks() {
        val userId = _state.value.userId
        val bookShelf = _state.value.bookShelf
        if (userId != null && bookShelf != null) {
            viewModelScope.launch(dispatchers.io) {
                getBookListUseCase.invoke(userId, bookShelf, null).collect { result ->
                    _state.update {
                        when (result) {
                            is Resource.Loading -> it.copy(uiState = UiState.Loading)
                            is Resource.Success -> it.copy(uiState = UiState.Success(result.data), books = result.data)
                            is Resource.Error -> it.copy(uiState = UiState.Error(result.exception))
                        }
                    }
                }
            }
        }
    }
}