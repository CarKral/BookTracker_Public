package com.example.book_tracker.features.feature_book.presentation._components

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookReading
import com.example.book_tracker.core.presentation.util.asSeconds
import com.example.book_tracker.core.presentation.util.dayAsStartOfDay
import com.example.book_tracker.core.presentation.util.roundTo
import com.example.book_tracker.core.presentation.util.todayAsStartOfDay
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Timestamp
import org.junit.Before
import org.junit.Test

class ReadingMathKtTest {
    private lateinit var book1: Book


    @Before
    fun setUp() {
        book1 = Book(
            // 1.1.2022
            addedDate = Timestamp(1672581209, 0),
            pageCount = 300,
            currentPage = 210.0,
            // 5.1.2022
            startedReading = Timestamp(1672926809, 0),
            // 25.1.2022
            finishedReading = Timestamp(1674654809, 0),
            goalReadingSpeed = 10.0,
            // 20.1.2022
            lastReading = BookReading(
                pageRangeList = listOf(140.0, 156.0), date = Timestamp(1674222809, 0)
            )
        )
    }

    @Test
    fun calculateReadingSpeed() {
        val days = calculateDayOfReading(book1.startedReading, book1.finishedReading)
        val speed = calculateReadingSpeed(
            currentPage = book1.currentPage,
            pageCount = book1.pageCount,
            startedReading = book1.startedReading,
            finishedReading = book1.finishedReading
        )
        assertThat(speed).isEqualTo(book1.currentPage!!.div(days).roundTo(2))
    }

    @Test
    fun calculateDayOfReading() {
        val days = book1.startedReading?.let { book1.finishedReading?.let { it1 -> calculateDayOfReading(it, it1) } }
        assertThat(days).isEqualTo(21)
    }

    @Test
    fun differenceBetweenDates() {
        val days = book1.startedReading?.let { book1.finishedReading?.let { it1 -> calculateDayOfReading(it, it1) } }
        assertThat(days).isEqualTo(21)
    }

    @Test
    fun calculateEstimateFinishedDays() {
        val pagesLeft = book1.pageCount!!.minus(book1.currentPage!!)
        val speed = calculateReadingSpeed(
            currentPage = book1.currentPage,
            pageCount = book1.pageCount,
            startedReading = book1.startedReading,
            finishedReading = book1.finishedReading
        )
        assertThat(calculateEstimateFinishedDays(pagesLeft, speed!!)).isEqualTo(9.0)
    }

    @Test
    fun calculateEstimateDate() {
        val pagesLeft = book1.pageCount!!.minus(book1.currentPage!!)
        val speed = calculateReadingSpeed(
            currentPage = book1.currentPage,
            pageCount = book1.pageCount,
            startedReading = book1.startedReading,
            finishedReading = book1.finishedReading
        )
        val estimateFinishedDays = calculateEstimateFinishedDays(pagesLeft, speed!!)

        assertThat(
            calculateEstimateDate(estimateFinishedDays)
        ).isEqualTo(
            Timestamp(todayAsStartOfDay.seconds.plus(estimateFinishedDays.minus(1).asSeconds()).toLong(), 0)
        )

        assertThat(calculateEstimateDate(null)).isNull()
        assertThat(calculateEstimateDate(20.0)).isNotNull()
    }


    @Test
    fun calculateGoalFinishedDays() {
        assertThat(calculateGoalFinishedDays(book1.pageCount!!.toDouble(), book1.goalReadingSpeed!!))
            .isEqualTo(30)
    }

    @Test
    fun calculateGoalFinishedDate() {
        val goalFinishedDays = calculateGoalFinishedDays(book1.pageCount!!.toDouble(), book1.goalReadingSpeed!!)
        assertThat(calculateGoalFinishedDate(goalFinishedDays)).isEqualTo(
            dayAsStartOfDay(Timestamp(todayAsStartOfDay.seconds.plus(goalFinishedDays.minus(1).asSeconds()).toLong(), 0))
        )

        assertThat(calculateGoalFinishedDate(null)).isNull()
        assertThat(calculateGoalFinishedDate(20.0)).isNotNull()
    }

    @Test
    fun getPagesFromPageRangeList() {
        assertThat(getPagesFromPageRangeList(book1.lastReading?.pageRangeList)).isEqualTo(16.0)
        assertThat(getPagesFromPageRangeList(listOf())).isNull()
        assertThat(getPagesFromPageRangeList(null)).isNull()
    }
}