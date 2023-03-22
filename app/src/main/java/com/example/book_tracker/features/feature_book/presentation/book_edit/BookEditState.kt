package com.example.book_tracker.features.feature_book.presentation.book_edit

import android.net.Uri
import androidx.work.WorkInfo
import com.example.book_tracker.core.domain.model.book.Book

data class BookEditState(
    val uiState: UiState = UiState.Loading,
    val bookId: String? = null,
    val book: Book = Book(),
    val isBookNew: Boolean = false,
    val isEdited: Boolean = false,
    val isImageEdited: Boolean = false,
    val status: Status = Status.IDLE,
    val imageUri: Uri? = null,
    val outputWorkInfoList: List<WorkInfo>?  = null,
) {
    enum class Status {
        SUCCESS, FAILED, LOADING, IDLE, SAVING
    }

    sealed interface UiState {
        object Loading : UiState
        data class Success(val data: Book?) : UiState
        data class Error(val throwable: Throwable? = null) : UiState
    }
}