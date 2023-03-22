package com.example.book_tracker.features.feature_book_list.presentation.my_book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.use_case.book.GetBookListUseCase
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.features.feature_book_list.presentation.my_book_list.BookListState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListScreenViewModel @Inject constructor(
    private val getBookListUseCase: GetBookListUseCase,
    private val dispatchers: DispatcherProvider
    ) : ViewModel() {

    private val _state = MutableStateFlow(BookListState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = BookListState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onBookShelfChanged(bookShelf: String?) {
        _state.update { it.copy(bookShelf = bookShelf) }
    }

    fun getBooks() {
        _state.value.bookShelf?.let {
            viewModelScope.launch(dispatchers.io) {
                getBookListUseCase(null, it, null).collect { result ->
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