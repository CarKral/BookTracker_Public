package com.example.book_tracker.features.feature_book.presentation._components

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AddReadingSectionKtTest {

    @Test
    fun `new page from double rounded to int`() {
        assertThat(getNewPage(newPage = 0.0, addition = true)).isEqualTo(1)
        assertThat(getNewPage(newPage = 10.0, addition = true)).isEqualTo(11)
        assertThat(getNewPage(newPage = 5.5, addition = true)).isEqualTo(6)
        assertThat(getNewPage(newPage = 56.1, addition = true)).isEqualTo(57)

//        assertThat(getNewPage(newPage = 5.1, addition = false)).isEqualTo(5)
        assertThat(getNewPage(newPage = 5.9, addition = false)).isEqualTo(5)
        assertThat(getNewPage(newPage = 1.0, addition = false)).isEqualTo(0)
        assertThat(getNewPage(newPage = 10.0, addition = false)).isEqualTo(9)
        assertThat(getNewPage(newPage = 1.5, addition = false)).isEqualTo(1)
    }
}