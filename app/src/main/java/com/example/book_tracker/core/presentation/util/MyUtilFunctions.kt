package com.example.book_tracker.core.presentation.util

import android.text.Html
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor

/** @return Floored number (Double or Int). Check if given Double ends on ".0" and then floor it if is true.
 * Example: floorDoubleToInt(1.5) => 1.5; floorDoubleToInt(1.0) = > 1 */
fun Double.floorDoubleToInt(): Any {
    return this.let { if (it == floor(it)) it.toInt() else it.roundTo(2) }
}

/** @return Given number as Double rounded to given decimal places.   */
fun Double.roundTo(decimals: Int): Double {
    return this.toBigDecimal().setScale(decimals, RoundingMode.HALF_EVEN).toDouble()
}

/** @return String with replaced comma to dot. Used for translate String "decimal" to Double. */
fun String.replaceCommaToDot(): String {
    return this.replace(",", ".")
}

/** @return Given DAYS converted to seconds. Formula: days * 86400 */
fun Double.asSeconds(): Double {
    return this * 86400
}

/** @return Given seconds converted to days. Formula: seconds / 86400 */
fun Long.toDays(): Long {
    return this.div(86400)
}

/** Checks if given String is empty or if its value is equal to 0.0 */
fun String.isZeroOrEmpty(): Boolean {
    return this.isEmpty() || this.toDoubleOrNull() == 0.0
}

/** Checks if given Double is null or if its value is equal to 0.0 */
fun Double?.isZeroOrNull(): Boolean {
    return this == null || this == 0.0
}

/** @return Given Html converted to String */
fun htmlToStringConverter(html: String?): String {
    return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
}


/** @return Given number as formatted String with DecimalFormat of "#.#" and with replaced comma to dot. */
fun roundedDecimalFormat(number: Any?): String {
    return number?.let {
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.UP
        return decimalFormat.format(number).replaceCommaToDot()
    } ?: ""
}

