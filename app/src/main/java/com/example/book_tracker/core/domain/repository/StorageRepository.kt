package com.example.book_tracker.core.domain.repository

import android.net.Uri


interface StorageRepository {

//    suspend fun getImageFromStorage(): Resources<Flow<>>
    suspend fun uploadImageToStorage(uri: Uri, onSuccess:(resultUri: Uri) -> Unit)

}