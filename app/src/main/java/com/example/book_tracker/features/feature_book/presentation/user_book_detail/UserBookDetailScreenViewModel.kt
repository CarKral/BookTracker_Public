package com.example.book_tracker.features.feature_book.presentation.user_book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.use_case.book.BookUseCases
import com.example.book_tracker.core.domain.util.DefaultDispatchers
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.core.domain.util.mapToMewBook
import com.example.book_tracker.features.feature_book.presentation.user_book_detail.UserBookDetailState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserBookDetailScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookUseCase: BookUseCases,
    private val dispatchers: DispatcherProvider

) : ViewModel() {

    private val _state = MutableStateFlow(UserBookDetailState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = UserBookDetailState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onIsAddedChanged(value: Boolean) {
        _state.update { it.copy(isAddedToLibrary = value) }
    }

    fun onMyBookChanged(book: Book?) {
        _state.update { it.copy(book = book) }
    }

    fun onUserIdChanged(id: String?) {
        _state.update { it.copy(userId = id) }
    }

    fun onBookIdChanged(id: String?) {
        _state.update { it.copy(bookId = id) }
    }

    fun getBook() {
        val userId = _state.value.userId
        val bookId = _state.value.bookId

        if (userId != null && bookId != null) {
            viewModelScope.launch(dispatchers.io) {
                bookUseCase.getBookUseCase(userId, bookId).collect { result ->
                    _state.update {
                        when (result) {
                            is Resource.Loading -> it.copy(uiState = UiState.Loading)
                            is Resource.Success -> it.copy(uiState = UiState.Success(result.data), book = result.data)
                            is Resource.Error -> it.copy(uiState = UiState.Error(result.exception))
                        }
                    }
                }
            }
        }
    }

    fun addBookToLibrary(onSuccess: (book: Book?) -> Unit, onExist: (Boolean) -> Unit) {
        viewModelScope.launch(dispatchers.io) {
            bookUseCase.addBookUseCase(
                book = _state.value.book.mapToMewBook(authRepository.currentUserId),
                onSuccess = onSuccess,
                onExist = onExist
            )
        }
    }
}

