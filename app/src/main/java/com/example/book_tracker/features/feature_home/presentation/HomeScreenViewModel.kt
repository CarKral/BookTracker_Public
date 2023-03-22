package com.example.book_tracker.features.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.use_case.book.GetBookListUseCase
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.features.feature_home.presentation.HomeState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getBookListUseCase: GetBookListUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow().stateIn(
            scope = viewModelScope,
            initialValue = HomeState(uiState = UiState.Loading),
            started = SharingStarted.WhileSubscribed(5000L)
        )

    init {
        getBooks()
    }

    fun getBooks() {
        viewModelScope.launch(dispatchers.io) {
            getBookListUseCase(null, BookShelf.CurrentRead.toString(), null).collect { result ->
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

//    var books by mutableStateOf<MutableList<MyBook>?>(null)
//        private set
//    var reorderableBooks by mutableStateListOf<Flow<List<MyBook>>?>(null)
//        private set
//    var currentDraggedIndex by mutableStateOf<Int?>(null)
//        private set
//    var itemPositionBefore: Int? = null
//    var itemPositionAfter: Int? = null
//    var itemPositionCurrent: Int? = null

//    fun onBooksChanged(list: List<MyBook>?) {
//        books = list?.toMutableStateList()
//    }

//    fun bookPositionChanged(fromIndex: Int, toIndex: Int) {
//        books?.let {
//            if (fromIndex == toIndex) return
//
//            currentDraggedIndex = toIndex
//            val element = it.removeAt(fromIndex)
//            it.add(toIndex, element)
//
//            println("MOVE FROM $fromIndex TO $toIndex ENE NOW")
//        }
//    }

//    fun bookPositionDragEnd() {
//        viewModelScope.launch {
//
//            println("DRAG TO $currentDraggedIndex END")
////            println("DRAGGED BOOK ${books?.get(currentDraggedIndex!!).toString()} ")
//            currentDraggedIndex?.let {
//                var book = books?.get(currentDraggedIndex!!)
////                val newPosition = makeMeNewPosition()
////                val newPosition = Random.nextInt(10000..99999)
////                println("NEW POSITION $newPosition")
//
//                books?.forEach {
//                    println("ALL POSITIONS      ${it.title + "    " + it.position.toString()}")
//                }
//
//                book = book?.copy(position = makeMeNewPosition())
//
//                firestoreRepository.updateBookPositionAtDatabase(book = book, onSuccess = {})
//            }
//        }
//    }

//    @OptIn(ExperimentalStdlibApi::class)
//    private fun makeMeNewPosition(): Int {
//        books?.let { books ->
//            currentDraggedIndex?.let { currentIndex ->
//                when (currentIndex) {
//                    0 -> {
//                        books[1].position.let { itemAfter ->
//                            return Random.nextInt(0 ..< itemAfter!!)
//                        }
//                    }
//                    books.size.minus(1) -> {
//                        books.last().position.let { itemBefore ->
//                            return Random.nextInt(itemBefore!! ..<100000)
//                        }
//                    }
//                    else -> {
//                        books[currentIndex.minus(1)].position?.let { itemBefore ->
//
//                            books[currentIndex.plus(1)].position?.let { itemAfter ->
//
//                                return Random.nextInt(itemBefore ..<itemAfter)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return itemPositionCurrent ?: 123456
//    }


}