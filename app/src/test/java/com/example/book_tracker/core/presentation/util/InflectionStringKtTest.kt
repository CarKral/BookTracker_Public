package com.example.book_tracker.core.presentation.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test
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

class InflectionStringKtTest {

    /**
     * @see Any.inflectionDay
     */
    @Test
    fun inflectionDay() {
        // "1 den"
        assertThat(1.inflectionDay()).isEqualTo("1 $INFLECTION_DAY_DEN")
        assertThat(1.0.inflectionDay()).isEqualTo("1 $INFLECTION_DAY_DEN")
        assertThat(0.5.inflectionDay()).isEqualTo("1 $INFLECTION_DAY_DEN")
        assertThat("0,5".inflectionDay()).isEqualTo("1 $INFLECTION_DAY_DEN")
        // "2, 3, 4 dny"
        for (day in 2..4) {
            assertThat(day.inflectionDay()).isEqualTo("$day $INFLECTION_DAY_DNY")
        }

        // "5, 6, 7, .. dny"
        for (day in 5..50) {
            assertThat(day.inflectionDay()).isEqualTo("$day $INFLECTION_DAY_DNI")
        }
        assertThat("999".inflectionDay()).isEqualTo("999 $INFLECTION_DAY_DNI")
        assertThat(5.999.inflectionDay()).isEqualTo("6 $INFLECTION_DAY_DNI")
        assertThat(5.2.inflectionDay()).isEqualTo("5 $INFLECTION_DAY_DNI")
        assertThat("5,2".inflectionDay()).isEqualTo("5 $INFLECTION_DAY_DNI")

    }

    /**
     * @see Any.inflectionDay2
     */
    @Test
    fun inflectionDay2() {
        // "dnes"
        assertThat(0.inflectionDay2()).isEqualTo(INFLECTION_DAY2_DNES)
        assertThat((0.5).inflectionDay2()).isEqualTo(INFLECTION_DAY2_DNES)
        assertThat(("0.5").inflectionDay2()).isEqualTo(INFLECTION_DAY2_DNES)
        assertThat(("0,5").inflectionDay2()).isEqualTo(INFLECTION_DAY2_DNES)
        assertThat((0.8).inflectionDay2()).isEqualTo(INFLECTION_DAY2_DNES)

        // "včera"
        assertThat(1.inflectionDay2()).isEqualTo(INFLECTION_DAY2_VCERA)

        // "před 2, 3, 4, 5, .. dny"
        for (day in 2..50) {
            assertThat(day.inflectionDay2()).isEqualTo("$INFLECTION_DAY2_PRED $day $INFLECTION_DAY2_DNY")
        }

        assertThat(10.99.inflectionDay2()).isEqualTo("$INFLECTION_DAY2_PRED 10 $INFLECTION_DAY2_DNY")
        assertThat(11.5.inflectionDay2()).isEqualTo("$INFLECTION_DAY2_PRED 11 $INFLECTION_DAY2_DNY")

    }

    /**
     * @see Any.inflectionPage
     */
    @Test
    fun inflectionPage() {
        // "1 strana"
        assertThat(1.inflectionPage()).isEqualTo("1 $INFLECTION_PAGE_STRANA")
        assertThat(1.0.inflectionPage()).isEqualTo("1 $INFLECTION_PAGE_STRANA")

        // "2, 3, 4 strany"
        for (page in 2..4) {
            assertThat(page.inflectionPage()).isEqualTo("$page $INFLECTION_PAGE_STRANY")
        }

        // "5, 6, .. stran"
        for (page in 5..50) {
            assertThat(page.inflectionPage()).isEqualTo("$page $INFLECTION_PAGE_STRAN")
            assertThat(page.toString().inflectionPage()).isEqualTo("$page $INFLECTION_PAGE_STRAN")
        }

        assertThat(0.1.inflectionPage()).isEqualTo("0.1 $INFLECTION_PAGE_STRAN")
        assertThat(0.9.inflectionPage()).isEqualTo("0.9 $INFLECTION_PAGE_STRAN")
        assertThat("0.8".inflectionPage()).isEqualTo("0.8 $INFLECTION_PAGE_STRAN")
        assertThat("0,8".inflectionPage()).isEqualTo("0.8 $INFLECTION_PAGE_STRAN")
        assertThat(566.inflectionPage()).isEqualTo("566 $INFLECTION_PAGE_STRAN")
        assertThat("".inflectionPage()).isEqualTo("")

    }
}
