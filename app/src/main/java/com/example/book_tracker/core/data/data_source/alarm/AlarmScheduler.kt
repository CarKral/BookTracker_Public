package com.example.book_tracker.core.data.data_source.alarm


/**
 * interface for AndroidAlarmScheduler class
 * @see AndroidAlarmScheduler
 * */
interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}