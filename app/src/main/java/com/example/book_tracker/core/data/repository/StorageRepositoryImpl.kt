package com.example.book_tracker.core.data.repository

import android.content.Context
import android.net.Uri
import com.example.book_tracker.core.domain.repository.StorageRepository
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: StorageReference,
    @ApplicationContext val context: Context,
) : StorageRepository {

    override suspend fun uploadImageToStorage(uri: Uri, onSuccess: (uri: Uri) -> Unit) {

        val reference = storage.child("images/${uri.lastPathSegment}")
//
//        var bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
//            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
//        else MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
//
//
//        val baos = ByteArrayOutputStream()
//        val scaledHeight = 600
//        val scaledWidth = bitmap.width.toDouble().div(bitmap.height.toDouble().div(scaledHeight))
//        bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth.roundToInt(), scaledHeight, false)
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val data = baos.toByteArray()

        reference.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) task.exception?.let { throw it }
            reference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) onSuccess(task.result)
            else println(task.exception?.message)
        }.addOnFailureListener {
            println("Something wrong with upload file to storage. E: ${it.message}")
        }
    }
}
