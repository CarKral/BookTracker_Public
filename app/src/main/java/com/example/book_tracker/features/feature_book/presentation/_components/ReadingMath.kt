package com.example.book_tracker.features.feature_book.presentation._components

import com.example.book_tracker.core.presentation.util.*
import com.google.firebase.Timestamp
import kotlin.math.abs
import kotlin.math.roundToInt

/** @return Rychlost čtení. Vzorec pro výpočet: (currentPage)/(todayAsStartOfDay - startedReading)
 * @param currentPage Number of current reading page of the book
 * @param pageCount Number of total pages of the book
 * */
fun calculateReadingSpeed(
    currentPage: Double?,
    pageCount: Int?,
    startedReading: Timestamp?,
    finishedReading: Timestamp?
): Double? {
    return if (currentPage?.toInt() == 0) 0.0
    else if (pageCount != null && currentPage != null && startedReading != null) {
        var days = calculateDayOfReading(startedReading, finishedReading)
        if (days == 0) days = 1
        (currentPage).div(days).roundTo(2)
    } else null
}

/** @return Celkový počet čtecích dní. Vzorec pro výpočet: (todayAsStartOfDay - startedReading) */
fun calculateDayOfReading(startedReading: Timestamp?, finishedReading: Timestamp?): Int {
    if (startedReading == null) return 0

    return if (todayAsStartOfDay == dayAsStartOfDay(startedReading)) 1
    /** Vypočítá počet čtecích dní pro dočtenou knihu. */
    else if (finishedReading != null) differenceBetweenDates(startedReading, finishedReading)
    else differenceBetweenDates(startedReading, todayAsStartOfDay)
}

/** @return Rozdíl mezi dvěma daty. */
fun differenceBetweenDates(first: Timestamp, second: Timestamp): Int {
    //*  dayAsStartOfDay(timestamp) je provedeno pro porovnávání dnů jako START OF THE DAY. */
    val firstDay = dayAsStartOfDay(first).seconds
    val secondDay = dayAsStartOfDay(second).seconds
    if (firstDay == secondDay) return 1
    //* Vypočítá počet čtecích dní včetně prvního dne (proto .plus(1)). */
    return abs(firstDay.minus(secondDay)).toDays().plus(1).toDouble().roundToInt()
}

/** @return Počet dní k odhadovanému dočtení */
fun calculateEstimateFinishedDays(pagesLeft: Double, currentReadingSpeed: Double): Double {
    return pagesLeft.div(currentReadingSpeed)
}

/** @return Datum odhadovaného dočtení knihy */
fun calculateEstimateDate(estimateFinishedDays: Double?): Timestamp? {
    return if (estimateFinishedDays == null || estimateFinishedDays == 0.0 || estimateFinishedDays == Double.POSITIVE_INFINITY) null
    else dayAsStartOfDay(
        Timestamp(
            todayAsStartOfDay.seconds.plus(estimateFinishedDays.minus(1).asSeconds()).toLong(),
            0
        )
    )
}

/** @return Počet dní k nastavenému dočtení */
fun calculateGoalFinishedDays(pagesTotal: Double, goalSpeed: Double): Double {
    return pagesTotal.div(goalSpeed)
}

/** @return Datum cílového dne dočtení (finishedDays je počet dní dočtení včetně dnešního dne. Ten je zahrnutý ale i zde -> proto finishedDays.minus(1))
 * Př. Dnes je 1.1., finishedDays = 5 => datum dočtení je 5.1. (doba četby je 1.-5.1.)
 * */
fun calculateGoalFinishedDate(goalFinishedDays: Double?): Timestamp? {
    return if (goalFinishedDays == null || goalFinishedDays == 0.0) null
    else dayAsStartOfDay(Timestamp(todayAsStartOfDay.seconds.plus(goalFinishedDays.minus(1).asSeconds()).toLong(), 0))
}

/**
 * @return Počet stran z listu "pageRangeList". MyBook - lastReading */
fun getPagesFromPageRangeList(list: List<Double?>?): Double? {
    return if (list.isNullOrEmpty()) null
    else list[1]?.minus(list[0] ?: 0.0) ?: 0.0
}

