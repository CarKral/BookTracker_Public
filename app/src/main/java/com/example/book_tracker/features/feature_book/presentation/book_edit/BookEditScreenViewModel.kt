package com.example.book_tracker.features.feature_book.presentation.book_edit

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.book_tracker.core.data.data_source.work_manager.CleanupWorker
import com.example.book_tracker.core.data.data_source.work_manager.ImageScaleWorker
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.repository.BookRepository
import com.example.book_tracker.core.domain.repository.StorageRepository
import com.example.book_tracker.core.domain.use_case.book.BookUseCases
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.example.book_tracker.core.presentation.util.Constants.IMAGE_SCALE_WORK
import com.example.book_tracker.core.presentation.util.Constants.KEY_IMAGE_URI
import com.example.book_tracker.core.presentation.util.Constants.TAG_OUTPUT
import com.example.book_tracker.features.feature_book.presentation.book_edit.BookEditState.Status
import com.example.book_tracker.features.feature_book.presentation.book_edit.BookEditState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookEditScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val workManager: WorkManager,
    private val bookRepository: BookRepository,
    private val bookUseCase: BookUseCases,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val currentUserId = authRepository.currentUserId

    private val _state = MutableStateFlow(BookEditState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = BookEditState(uiState = UiState.Loading),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun setWorkInfoList(workInfoList: List<WorkInfo>?) {
        _state.update { it.copy(outputWorkInfoList = workInfoList) }
    }

    fun getWorkInfoList(): LiveData<List<WorkInfo>?> {
        return workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    fun onSavingChanged(status: Status) {
        _state.update { it.copy(status = status) }
    }

    fun onIsBookNewChanged(boolean: Boolean) {
        _state.update { it.copy(isBookNew = boolean) }
    }

    fun onBookIdChanged(id: String?) {
        _state.update { it.copy(bookId = id) }
    }

    fun onMyBookChanged(book: Book) {
        _state.update { it.copy(book = book, isEdited = true) }
    }

    fun getBook() {
        _state.value.bookId?.let {
            viewModelScope.launch(dispatchers.io) {
                bookUseCase.getBookUseCase(null, it).collect { result ->
                    _state.update {
                        when (result) {
                            is Resource.Loading -> it.copy(uiState = UiState.Loading)
                            is Resource.Success -> {
                                it.copy(
                                    uiState = UiState.Success(result.data),
                                    book = result.data ?: Book(),
                                    imageUri = result.data?.photoUrl?.let { url -> Uri.parse(url) },
//                                    status = if (result.data != null) Status.SUCCESS else Status.IDLE
                                )

                            }
                            is Resource.Error -> it.copy(uiState = UiState.Error(result.exception))
                        }
                    }
                }
            }
        }
    }

    fun saveBookToDatabase(onSuccess: () -> Unit) {
        _state.update { it.copy(status = Status.SAVING) }
        viewModelScope.launch(dispatchers.io) {
            if (_state.value.imageUri != null && _state.value.isImageEdited) {
                uploadImageToStorage(
                    uri = _state.value.imageUri!!,
                    onSuccess = {
                        viewModelScope.launch(dispatchers.io) {
                            updateBookAtDatabase(onSuccess)
                        }
                    })
            } else {
                updateBookAtDatabase(onSuccess)
            }
        }
    }

    fun addNewBookToLibrary(onSuccess: (book: Book?) -> Unit) {
        _state.update { it.copy(status = Status.SAVING) }

        _state.update {
            it.copy(
                book = it.book.copy(
                    userId = currentUserId,
                    creatorId = currentUserId,
                    bookShelf = BookShelf.Unclassified.toString()
                )
            )
        }

        viewModelScope.launch(dispatchers.io) {
            if (_state.value.imageUri != null && _state.value.isImageEdited) {

                uploadImageToStorage(_state.value.imageUri!!, onSuccess = {
                    viewModelScope.launch(dispatchers.io) {
                        bookUseCase.addBookUseCase(
                            _state.value.book, onSuccess = { onSuccess(it) }, onExist = { },
                        )
//
                    }
                })
            } else {
                bookUseCase.addBookUseCase(
                    _state.value.book, onSuccess = { onSuccess(it) }, onExist = { },
                )
            }
        }
    }

    private suspend fun uploadImageToStorage(uri: Uri, onSuccess: () -> Unit) {
        storageRepository.uploadImageToStorage(uri, onSuccess = { resultUri ->
            println("Upload to Storage complete")
            /* Update photoUrl field to new uploaded image uri */
            _state.update { it.copy(book = it.book.copy(photoUrl = resultUri.toString())) }
            onSuccess()
        })
    }

    private suspend fun updateBookAtDatabase(onSuccess: () -> Unit) {
        bookRepository.updateBook(_state.value.book,
            onSuccess = {
                println("Update at database complete")
                onSuccess()
            })
    }

    fun deleteMyBook(onSuccess: () -> Unit) {
        viewModelScope.launch(dispatchers.io) {
            _state.value.book.id?.let {
                bookRepository.deleteBook(it, onSuccess = onSuccess)
            }
        }
    }

    fun applyImageScale(uri: Uri) {
        _state.update { it.copy(isEdited = true, isImageEdited = true, imageUri = uri) }
        var continuation = workManager
            .beginUniqueWork(
                IMAGE_SCALE_WORK,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.Companion.from(CleanupWorker::class.java)
            )

        val imageScaleRequest = OneTimeWorkRequest.Builder(ImageScaleWorker::class.java)
            .setInputData(createInputDataForUri(uri))
            .addTag(TAG_OUTPUT)
            .build()

        continuation = continuation.then(imageScaleRequest)
        continuation.enqueue()
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(uri: Uri): Data {
        val builder = Data.Builder()
        _state.value.imageUri?.let {
            builder.putString(KEY_IMAGE_URI, it.toString())
        }
        return builder.build()
    }

    internal fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_SCALE_WORK)
    }
}

