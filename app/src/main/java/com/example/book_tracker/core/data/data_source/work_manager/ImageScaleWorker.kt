package com.example.book_tracker.core.data.data_source.work_manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.book_tracker.core.presentation.util.Constants.KEY_IMAGE_URI
import com.example.book_tracker.core.presentation.util.Constants.OUTPUT_PATH
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.math.roundToInt

/**
 * ImageScaleWorker is used for scaling image when user edit Book's image source
 * */
class ImageScaleWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val resolver = applicationContext.contentResolver
            var bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(resolver, Uri.parse(resourceUri)))
            else MediaStore.Images.Media.getBitmap(resolver, Uri.parse(resourceUri))

            // Scaled bitmap height is fixed to 600
            val scaledHeight = 600
            // Scaled bitmap width calculated for keep the same ration of the original image
            val scaledWidth = bitmap.width.toDouble().div(bitmap.height.toDouble().div(scaledHeight))

            var outputStream: FileOutputStream? = null
            val name = String.format("image-%s.png", UUID.randomUUID().toString())
            val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
            if (!outputDir.exists()) { outputDir.mkdirs() }
            val outputFile = File(outputDir, name)

            try {
                outputStream = FileOutputStream(outputFile)
                bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth.roundToInt(), scaledHeight, false)
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
            } finally {
                outputStream?.close()
            }

            val outputUri = Uri.fromFile(outputFile)
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(outputData)
        } catch (throwable: Throwable) {
            Timber.tag("TAG").e("Error scaling image -  ${throwable.message}")
            Result.failure()
        }
    }
}