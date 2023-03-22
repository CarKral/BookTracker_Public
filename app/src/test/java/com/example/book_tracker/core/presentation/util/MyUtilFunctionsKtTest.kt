package com.example.book_tracker.core.presentation.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MyUtilFunctionsKtTest {

    /**
     * @see Double.floorDoubleToInt
     */
    @Test
    fun `Floor double to Int, if possible`() {
        assertThat(1.0.floorDoubleToInt()).isEqualTo(1)
        assertThat(1.23456.floorDoubleToInt()).isEqualTo(1.23)
        assertThat("1.0".toDouble().floorDoubleToInt()).isEqualTo(1)
    }

    /**
     * @see Double.roundTo
     */
    @Test
    fun `Round double to given decimals`() {
        assertThat(1.28.roundTo(1)).isEqualTo(1.3)
        assertThat(5.655.roundTo(2)).isEqualTo(5.66)
        assertThat(1.23456.roundTo(3)).isEqualTo(1.235)
        assertThat(1.23456.roundTo(4)).isEqualTo(1.2346)
        assertThat(2.656555.roundTo(2)).isEqualTo(2.66)
    }

    /**
     * @see String.replaceCommaToDot
     */
    @Test
    fun `Replace comma at String to dot`() {
        assertThat("1,5".replaceCommaToDot()).isEqualTo("1.5")
        assertThat("111,5".replaceCommaToDot()).isEqualTo("111.5")
        assertThat("1,0".replaceCommaToDot()).isEqualTo("1.0")
    }


    /**
     * @see String.isZeroOrEmpty
     */
    @Test
    fun `Checks if given String is zero or empty`() {
        assertThat("0.0".isZeroOrEmpty()).isTrue()
        assertThat("".isZeroOrEmpty()).isTrue()
        assertThat("abcd".isZeroOrEmpty()).isFalse()
    }

    /**
     * @see Double.isZeroOrNull
     */
    @Test
    fun `Checks if given Double is zero or null`() {
        val nullDouble: Double? = null
        assertThat(0.0.isZeroOrNull()).isTrue()
        assertThat(nullDouble.isZeroOrNull()).isTrue()
        assertThat((1.0.isZeroOrNull())).isFalse()
    }

    /**
     * @see Double.asSeconds
     */
    @Test
    fun `Converts given Double to seconds`() {
        val double = 10.0
        assertThat(double.asSeconds()).isEqualTo(double * 86400)
    }

    /**
     * @see Long.toDays
     */
    @Test
    fun `Converts given Long to days`() {
        val long = 86400000000000
        assertThat(long.toDays()).isEqualTo(long.div(86400))
    }
}