package com.example.book_tracker.core.data.repository

import android.net.Uri
import com.example.book_tracker.core.domain.repository.StorageRepository

class FakeStorageRepositoryImpl() : StorageRepository {
    override suspend fun uploadImageToStorage(uri: Uri, onSuccess: (resultUri: Uri) -> Unit) {
        TODO("Not yet implemented")
    }

}
