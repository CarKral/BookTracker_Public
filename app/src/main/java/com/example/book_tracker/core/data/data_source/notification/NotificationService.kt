package com.example.book_tracker.core.data.data_source.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.book_tracker.R
import com.example.book_tracker.features.feature_main.presentation.MainActivity
import javax.inject.Inject


/**
 *
 *
 * */
class NotificationService @Inject constructor(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showReadingGoalReminderNotification() {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            BOOK_GOAL_NOTIFICATION_ID,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, BOOK_GOAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_menu_book_24)
            .setContentTitle("Už jsi dnes četl?")
            .setContentText("Pokud ne, s úsměvem do toho!")
            .setContentIntent(activityPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .addAction(R.drawable.ic_pinto_foreground, "DONE" , null /*updateNote*/)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(BOOK_GOAL_NOTIFICATION_ID, notification)
    }

    companion object {
        const val BOOK_GOAL_CHANNEL_ID = "book_tracker_goal_channel"
        const val BOOK_GOAL_NOTIFICATION_ID = 666
    }

}