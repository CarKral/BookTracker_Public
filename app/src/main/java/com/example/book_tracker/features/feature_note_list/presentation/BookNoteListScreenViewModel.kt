package com.example.book_tracker.features.feature_note_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.book.BookNote
import com.example.book_tracker.core.domain.repository.NoteRepository
import com.example.book_tracker.core.domain.use_case.note.GetNoteListUseCase
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.features.feature_note_list.presentation.BookNoteListState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookNoteListScreenViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val getNoteListUseCase: GetNoteListUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(BookNoteListState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = BookNoteListState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onBookIdChanged(id: String?) {
        _state.update { it.copy(bookId = id) }
    }

    fun addNote(onSuccess: () -> Unit) {

        viewModelScope.launch(dispatchers.io){
            noteRepository.addNote(
                note = BookNote(
                    bookId = _state.value.bookId,
                    text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Curabitur vitae diam non enim vestibulum interdum. Mauris metus. Etiam sapien elit, consequat eget, tristique non, venenatis quis, ante. In enim a arcu imperdiet malesuada. Lorem ipsum dolor sit amet, consectetuer adipiscing elit."
                ),
                onSuccess = {
                    onSuccess()
//                    isLoading = false
                }
            )
        }
    }

    fun getNotes() {
        _state.value.bookId?.let {
            viewModelScope.launch(dispatchers.io) {
                getNoteListUseCase(it).collect { result ->
                    _state.update {
                        when (result) {
                            is Resource.Loading -> it.copy(uiState = UiState.Loading)
                            is Resource.Success -> it.copy(uiState = UiState.Success(result.data), notes = result.data)
                            is Resource.Error -> it.copy(uiState = UiState.Error(result.exception))
                        }
                    }
                }
            }
        }
    }
}