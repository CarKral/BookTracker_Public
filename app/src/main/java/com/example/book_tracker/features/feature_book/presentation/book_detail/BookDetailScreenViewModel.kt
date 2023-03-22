package com.example.book_tracker.features.feature_book.presentation.book_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookReading
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.repository.BookRepository
import com.example.book_tracker.core.domain.repository.ReadingRepository
import com.example.book_tracker.core.domain.use_case.book.GetBookUseCase
import com.example.book_tracker.core.domain.use_case.reading.GetReadingListUseCase
import com.example.book_tracker.core.domain.util.DefaultDispatchers
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.core.presentation.util.roundTo
import com.example.book_tracker.features.feature_book.presentation.book_detail.BookDetailState.UiState
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailScreenViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private val readingRepository: ReadingRepository,
    private val getBookUseCase: GetBookUseCase,
    private val getReadingListUseCase: GetReadingListUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(BookDetailState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = BookDetailState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onBookChanged(newBook: Book) {
        _state.update { it.copy(book = newBook) }
    }

    fun onBookIdChanged(id: String?) {
        _state.update { it.copy(bookId = id) }
    }

    fun onNewPageChanged(page: String?) {
        _state.update {
            it.copy(
                newPage = when (page?.toDoubleOrNull()) {
                    null -> ""
                    else -> page
                }
            )
        }
    }


    fun getReadings() {
        _state.value.bookId?.let {
            viewModelScope.launch(dispatchers.io) {
                getReadingListUseCase.invoke(it).collect { result ->
                    if (result is Resource.Success) {
                        _state.update { it.copy(readings = result.data) }
                    }
                }
            }
        }
    }

    /** This function get book by given id from database and then update state by returned result.
     * @see GetBookUseCase */
    fun getBook() {
        _state.value.bookId?.let {
            viewModelScope.launch(dispatchers.io) {
                getBookUseCase.invoke(null, it).collect { result ->
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

    /** This function update book at database with updated Bookshelf field, atc. */
    fun addToBookShelf(bookShelf: BookShelf, onSuccess: () -> Unit) {
        _state.value.book?.let { book ->
            _state.update { it.copy(uiState = UiState.Loading) }
            if (bookShelf == BookShelf.CurrentRead) {
                book.startedReading = Timestamp.now()
                if (book.bookShelf != BookShelf.Done.toString())
                    book.currentPage = book.currentPage ?: 0.0
                else book.currentPage = 0.0
            }
            if (bookShelf == BookShelf.Done) {
                book.finishedReading = Timestamp.now()
                book.currentPage = book.pageCount?.toDouble()
            }

            book.bookShelf = bookShelf.toString()

            viewModelScope.launch(dispatchers.io) {
                bookRepository.updateBook(
                    book = book,
                    onSuccess = { book ->
                        onSuccess()
                        _state.update { it.copy(uiState = UiState.Success(book)) }
                    }
                )
            }
        }
    }

    /** This function add new book note to database. */
    fun addNote(onSuccess: () -> Unit) {
        viewModelScope.launch(dispatchers.io) {
//            bookDataSource.addNoteToDatabase(
//                note = MyBookNote(
//                    bookId = _state.value.bookId,
//                    text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Curabitur vitae diam non enim vestibulum interdum. Mauris metus. Etiam sapien elit, consequat eget, tristique non, venenatis quis, ante. In enim a arcu imperdiet malesuada. Lorem ipsum dolor sit amet, consectetuer adipiscing elit."
//                ),
//                onSuccess = {
//                    onSuccess()
////                    isLoading = false
//                }
//            )
        }
    }

    /** This function add new book note to database. */
    fun addNewReading(bookId: String?, onSuccess: () -> Unit) {
        viewModelScope.launch (dispatchers.io){
            val oldPage = _state.value.book?.currentPage?.roundTo(2)
            val newPage = _state.value.newPage?.toDouble()?.roundTo(2)

            readingRepository.addNewReading(
                book = _state.value.book?.copy(
                    currentPage = newPage
                ),
                reading = BookReading(
//                    bookId = bookId,
//                    pages = _state.value.newPage?.toDouble()?.minus(_state.value.book?.currentPage ?: 0.0)?.round(2),
                    pageRangeList = listOf(oldPage, newPage)
                ),
                onSuccess = {
                    onSuccess()
//                    isLoading = false
                }
            )
        }
    }

    fun addNewReadingGoal(newBook: Book?, onSuccess: () -> Unit) {
        viewModelScope.launch(dispatchers.io) {
            bookRepository.addNewReadingGoal(
                book = newBook,
                onSuccess = {
                    onSuccess()
//                    isLoading = false
                }
            )
        }
    }

    fun deleteReadingGoal(onSuccess: () -> Unit) {
        viewModelScope.launch(dispatchers.io) {
            _state.value.book?.id?.let {
                bookRepository.deleteReadingGoal(it, onSuccess = onSuccess)
            }
        }
    }

    fun resetReading(onSuccess: () -> Unit) {
        viewModelScope.launch(dispatchers.io) {
            readingRepository.resetReading(
                book = _state.value.book,
                reading = BookReading(
                ),
                onSuccess = {
                    _state.update { it.copy(newPage = null) }
                    onSuccess()
                }
            )
        }
    }

    fun deleteMyBook(onSuccess: () -> Unit) {
        viewModelScope.launch(dispatchers.io) {
            _state.value.book?.id?.let {
                bookRepository.deleteBook(it, onSuccess = onSuccess)

            }
        }
    }
}