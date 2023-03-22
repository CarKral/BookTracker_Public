package com.example.book_tracker.core.data.data_source.work_manager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.book_tracker.core.presentation.util.Constants.OUTPUT_PATH
import java.io.File

class CleanupWorker(context: Context, params: WorkerParameters): Worker( context, params) {
    override fun doWork(): Result {
        return  try {

            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")){
                            val deleted = entry.delete()
//                            println("Deleted $entry - $deleted")
                        }
                    }
                }
            }
            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }
}