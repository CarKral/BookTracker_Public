package com.example.book_tracker.features.feature_library.presentation.my_library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.use_case.book.GetBookListUseCase
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.features.feature_library.presentation.my_library.LibraryState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryScreenViewModel @Inject constructor(
    private val getBookListUseCase: GetBookListUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = LibraryState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    init {
        getBooksByBookShelf()
    }

    private fun getBooksByBookShelf() {
        for (bookShelf in BookShelf.values()) {
            viewModelScope.launch(dispatchers.io) {
                getBookListUseCase.invoke(null, bookShelf.toString(), 2).collect { result ->
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

