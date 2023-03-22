package com.example.book_tracker.features.feature_reading_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.repository.FirestoreRepository
import com.example.book_tracker.core.domain.use_case.reading.GetReadingListUseCase
import com.example.book_tracker.core.domain.util.DefaultDispatchers
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.features.feature_reading_list.presentation.BookReadingListState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookReadingListScreenViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val getReadingListUseCase: GetReadingListUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(BookReadingListState())
    val state = _state.asStateFlow()


    fun onBookIdChanged(id: String?) {
        _state.update { it.copy(bookId = id) }
    }

    fun getReadings(){
        _state.value.bookId?.let {
            viewModelScope.launch(dispatchers.io) {
                getReadingListUseCase(it).collect { result ->
                    _state.update {
                        when (result) {
                            is Resource.Loading -> it.copy(uiState = UiState.Loading)
                            is Resource.Success -> it.copy(uiState = UiState.Success(result.data), readings = result.data)
                            is Resource.Error -> it.copy(uiState = UiState.Error(result.exception))
                        }
                    }
                }
            }
        }
    }
}