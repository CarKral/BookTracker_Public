package com.example.book_tracker.features.feature_book.presentation.book_detail_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.use_case.book.BookUseCases
import com.example.book_tracker.core.domain.util.DefaultDispatchers
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.core.domain.util.mapToMewBook
import com.example.book_tracker.features.feature_book.presentation.book_detail_search.BookDetailSearchState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookDetailSearchScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookUseCases: BookUseCases,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(BookDetailSearchState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = BookDetailSearchState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onBookIdChanged(id: String?) {
        _state.update { it.copy(bookId = id) }
    }

    fun getBook() {
        _state.value.bookId?.let {
            viewModelScope.launch(dispatchers.io) {
                bookUseCases.getItemUseCase(it).collect { result ->
                    _state.update {
                        when (result) {
                            is Resource.Loading -> it.copy(uiState = UiState.Loading)
                            is Resource.Success -> it.copy(uiState = UiState.Success(result.data), item = result.data)
                            is Resource.Error -> it.copy(uiState = UiState.Error(result.exception))
                        }
                    }
                }
            }
        }
    }

    fun addBookToLibrary(onSuccess: () -> Unit, onExist: (Boolean) -> Unit) {
        viewModelScope.launch(dispatchers.io) {
            _state.update { it.copy(book = _state.value.item.mapToMewBook(authRepository.currentUserId)) }
            _state.value.book?.let { book ->
                bookUseCases.addBookUseCase(
                    book,
                    onSuccess = { addedBook ->
                        _state.update { it.copy(book = addedBook, isAddedToLibrary = true) }
                        onSuccess()
                    },
                    onExist = { onExist(it) })
            }
        }
    }
}