package com.example.book_tracker.core.presentation.util

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

/** @return Today as start of the day */
val todayAsStartOfDay = dayAsStartOfDay(Timestamp.now())

/** @return Given day Timestamp as start of the day */
fun dayAsStartOfDay(timestamp: Timestamp) =
    Timestamp(localDateFromTimestamp(timestamp).atStartOfDay().toEpochSecond(ZoneOffset.UTC), 0)

/** @return Given Timestamp converted to LocalDateTime as formatted String */
fun localDateTimeStringFromTimestamp(timestamp: Timestamp?) = timestamp?.let {
    LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp.seconds),
        TimeZone.getDefault().toZoneId()
    ).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}

/** @return Given Timestamp converted to LocalDateTime */
fun localDateTimeFromTimestamp(timestamp: Timestamp): LocalDateTime =
    LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp.seconds),
        TimeZone.getDefault().toZoneId()
    )

/** @return Given Timestamp converted to LocalDate */
fun localDateFromTimestamp(timestamp: Timestamp): LocalDate =
    LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp.seconds),
        TimeZone.getDefault().toZoneId()
    ).toLocalDate()

/** @return Given LocalDateTime converted to Timestamp */
fun timestampFromLocalDateTime(dateTime: LocalDateTime): Timestamp =
    Timestamp(dateTime.toEpochSecond(ZoneOffset.UTC), 0)


/** @return Given LocalDate converted to Timestamp */
fun timestampFromLocalDate(dateTime: LocalDate): Timestamp =
    Timestamp(dateTime.atStartOfDay().toEpochSecond(ZoneOffset.UTC), 0)

/** @return Given LocalDate as formatted String  */
fun dateStringFromLocalDate(dateTime: LocalDate?) = dateTime?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
