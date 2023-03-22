package com.example.book_tracker.features.feature_search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.use_case.book.BookUseCases
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.core.domain.util.mapToMewBook
import com.example.book_tracker.features.feature_search.presentation.SearchState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val bookUseCases: BookUseCases,
    private val authRepository: AuthRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = SearchState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onOrderByNewestCHanged(value: Boolean) {
        _state.update { it.copy(orderByNewest = value) }
        searchItems()
    }

    fun onSearchQueryChanged(text: String?) {
        _state.update {
            it.copy(
                uiState = UiState.Loading,
                searchQuery = text,
            )
        }
        Timber.i("SearchQuery: --> $text")
        if (text.isNullOrEmpty()) {
            _state.update {
                it.copy(
                    uiState = UiState.Success(null),
                    items = emptyList(),
                    searchQuery = "",
                )
            }
        } else {
            _state.update { it.copy(searchQuery = text) }
            searchItems()
        }
    }

    fun searchItems() {
        if (_state.value.searchQuery.isNullOrEmpty()) return
        viewModelScope.launch(dispatchers.io) {
            _state.value.searchQuery?.let { query ->
                bookUseCases.getItemListUseCase(query, _state.value.orderByNewest).collect { result ->
                    _state.update {
                        when (result) {
                            is Resource.Loading -> it.copy(uiState = UiState.Loading)
                            is Resource.Success -> it.copy(uiState = UiState.Success(result.data), items = result.data)
                            is Resource.Error -> it.copy(uiState = UiState.Error(result.exception))
                        }
                    }
                }
            }
        }
    }

    fun addBookToDatabase(
        item: Item,
        onSuccess: (book: Book?) -> Unit,
        onExist: (Boolean) -> Unit,
    ) {
        viewModelScope.launch(dispatchers.io) {
            bookUseCases.getItemUseCase(item.id).collect { result ->
                if (result is Resource.Success) {
                    val book = result.data.mapToMewBook(authRepository.currentUserId)

                    bookUseCases.addBookUseCase(
                        book = book,
                        onSuccess = onSuccess,
                        onExist = onExist
                    )
                }
            }
        }
    }

//    fun removeBookFromDatabase(id: String) {
//        viewModelScope.launch {
//            bookRepository.deleteBook(id, onSuccess = { })
//        }
//    }
}

//    fun searchBookISBN(isbn: String?) {
//        viewModelScope.launch {
//            isbn?.let {
//                try {
//                    val booksState = bookRepository.getBookISBN(it)
////                    Timber.i(booksState.data.toString())
//                    when (booksState) {
//                        is Resources.Success -> {
//                            books = booksState.data ?: listOf()
//                            if (books.isNotEmpty()) isLoading = false
//                        }
//                        is Resources.Error -> {
//                            isLoading = false
//                            Timber.e("SearchBooks: Failed... ${booksState.message}")
//                        }
//                        else -> {
//                            isLoading = false
//                        }
//                    }
//                } catch (e: Exception) {
//                    isLoading = false
//                    Timber.e("SearchBooks: ${e.message.toString()}")
//                }
//            }
//        }
//    }
