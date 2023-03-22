package com.example.book_tracker.core.data.data_source.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.book_tracker.core.data.data_source.notification.NotificationService

/**
 * AlarmReceiver is BroadcastReceiver for AndroidAlarmScheduler
 * @see AndroidAlarmScheduler
 * */
class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
//        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return

        println("KKKKK - ALARM TRIGGERRED")

        context?.let {
            NotificationService(context).showReadingGoalReminderNotification()
        }
    }
}