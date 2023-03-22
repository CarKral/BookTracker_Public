package com.example.book_tracker.core.presentation.util

import com.example.book_tracker.core.presentation.util.InflectionStringConstants.INFLECTION_DAY2_DNES
import com.example.book_tracker.core.presentation.util.InflectionStringConstants.INFLECTION_DAY2_DNY
import com.example.book_tracker.core.presentation.util.InflectionStringConstants.INFLECTION_DAY2_PRED
import com.example.book_tracker.core.presentation.util.InflectionStringConstants.INFLECTION_DAY2_VCERA
import com.example.book_tracker.core.presentation.util.InflectionStringConstants.INFLECTION_DAY_DEN
import com.example.book_tracker.core.presentation.util.InflectionStringConstants.INFLECTION_DAY_DNI
import com.example.book_tracker.core.presentation.util.InflectionStringConstants.INFLECTION_DAY_DNY
import com.example.book_tracker.core.presentation.util.InflectionStringConstants.INFLECTION_PAGE_STRAN
import com.example.book_tracker.core.presentation.util.InflectionStringConstants.INFLECTION_PAGE_STRANA
import com.example.book_tracker.core.presentation.util.InflectionStringConstants.INFLECTION_PAGE_STRANY
import kotlin.math.roundToInt

/** @return Czech inflection of word "DAY" = "DEN", CZ-v1.
 * Example: "1 den", "2 dny", "3 dny", "4 dny", "5 dní", ...
 * */
fun Any.inflectionDay(): String {
    var day = this.toString().replaceCommaToDot()
    if (day.toDoubleOrNull() != null) {
        day = day.toDouble().roundToInt().toString()

        return when (day) {
            "1" -> "$day $INFLECTION_DAY_DEN"
            "2", "3", "4" -> "$day $INFLECTION_DAY_DNY"
            else -> "$day $INFLECTION_DAY_DNI"
        }
    }
    return ""
}

/** @return Czech inflection of word "DAY" = "DEN", CZ-v2.
 * Example: "Včera", "dnes", "před 1 dnem", "před 2 dny", "před 3 dny", "před 4 dny", ...
 * */
fun Any.inflectionDay2(): String {
    var day = this.toString().replaceCommaToDot()
    if (day.toDoubleOrNull() != null) {
        day = day.toDouble().toInt().toString()

        return when (day) {
            "0" -> INFLECTION_DAY2_DNES
            "1" -> INFLECTION_DAY2_VCERA
            else -> "$INFLECTION_DAY2_PRED $day $INFLECTION_DAY2_DNY"
        }
    }
    return ""
}

/** @return Czech inflection of word "PAGE" = "STRANA", CZ-v1.
 * 1 strana, 2 strany, 5 stran, 10 stran, ....
 * */
fun Any.inflectionPage(): String {
    val page = this.toString().replaceCommaToDot()
    if (page.toDoubleOrNull() != null) {

        return when (val flooredPage = page.toDouble().floorDoubleToInt()) {
            1 -> "$flooredPage $INFLECTION_PAGE_STRANA"
            2, 3, 4 -> "$flooredPage $INFLECTION_PAGE_STRANY"
            else -> "$flooredPage $INFLECTION_PAGE_STRAN"
        }
    }
    return ""
}