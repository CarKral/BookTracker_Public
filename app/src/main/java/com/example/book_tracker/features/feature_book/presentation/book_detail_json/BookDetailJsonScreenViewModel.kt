package com.example.book_tracker.features.feature_book.presentation.book_detail_json

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.use_case.book.GetItemUseCase
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.features.feature_book.presentation.book_detail_json.BookDetailJsonState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailJsonScreenViewModel @Inject constructor(
    private val getItemUseCase: GetItemUseCase,
    private val dispatchers: DispatcherProvider
    ) : ViewModel() {

    private val _state = MutableStateFlow(BookDetailJsonState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = BookDetailJsonState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onBookIdChanged(id: String?) {
        _state.update { it.copy(bookId = id) }
    }

     fun getBook() {
        _state.value.bookId?.let { id ->
            viewModelScope.launch(dispatchers.io) {
                getItemUseCase.invoke(id).collect { result ->
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
}