package com.example.book_tracker.core.data.data_source.alarm

import java.time.LocalDateTime

/**
 * AlarmItem is a data class for AlarmScheduler
 * @see AndroidAlarmScheduler
 * @see AlarmScheduler
 * */
data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)