package com.example.book_tracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.book_tracker.core.data.data_source.alarm.AlarmItem
import com.example.book_tracker.core.data.data_source.alarm.AndroidAlarmScheduler
import com.example.book_tracker.core.data.data_source.notification.NotificationService
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@HiltAndroidApp
class BookTrackerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        createNotificationChannel()

        val scheduler = AndroidAlarmScheduler(this)
        val alarmItem =
            AlarmItem(
                time = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0, 0)),
                message = "message from scheduler"
            )
        scheduler.schedule(alarmItem)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationService.BOOK_GOAL_CHANNEL_ID, "BOOK_READING_GOAL",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "Used for showing notifications from the app"
            setShowBadge(true)
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

