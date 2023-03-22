package com.example.book_tracker.features.feature_book.presentation._components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.model.book.placeholderBook
import com.example.book_tracker.core.presentation.components.MyDoubleTextRow
import com.example.book_tracker.core.presentation.util.*
import com.example.book_tracker.features.feature_book.presentation.util.visibleReadingBookShelf


@Preview(showBackground = true)
@Composable
fun ReadingSectionPreview() {
    ReadingSection(
        book = placeholderBook,
        content = {}
    )
}

@Composable
fun ReadingSection(
    modifier: Modifier = Modifier,
    book: Book,
    content: @Composable () -> Unit
) {
    val currentPage = book.currentPage ?: 0.0

    val pageCount = book.pageCount?.toDouble() ?: 0.0
    val pageLeft = pageCount.minus(currentPage)

    /** Celkový počet čtecích dní. */
    val dayOfReading = calculateDayOfReading(book.startedReading, book.finishedReading)

    /** Zjistí zda je čtení již dokončeno, či ne. */
    val readingDone = book.pageCount?.let { it == book.currentPage?.toInt() } ?: false

    /** Výpočet rychlosti čtení */
    val readingSpeed =
        calculateReadingSpeed(book.currentPage, book.pageCount, book.startedReading, book.finishedReading)

    /** Odhadovaný počet dní k dočtení knihy  (pageCount-currentPage)/(readingSpeed). */
    val estimateFinishedDays =
        if (!readingDone && readingSpeed != null) calculateEstimateFinishedDays(
            pageLeft,
            readingSpeed
        ) else null

    /** Datum odhadovaného dočtení knihy. */
    val estimatedDateText =
        localDateTimeStringFromTimestamp(calculateEstimateDate(estimateFinishedDays))
            ?: Constants.infinityString

    /** Počet dní cílového dočtení knihy.  */
    val goalFinishedDays =
        book.goalReadingSpeed?.let { calculateGoalFinishedDays(pageLeft, it) }

    /** Datum cílového dočtení knihy. */
    val goalFinishedDateText =
        localDateTimeStringFromTimestamp(calculateGoalFinishedDate(goalFinishedDays))
            ?: Constants.infinityString

    /** Počet dní posledního čtení knihy.  */
    val lastReadingTime =
        book.lastReading?.date?.let {
            todayAsStartOfDay.seconds.minus(dayAsStartOfDay(it).seconds).toDouble().div(86400)
        }

//    /** Počet dní posledního čtení - text. */
//    val lastReadingTimeText = myBook.lastReading?.date?.let {
//        roundedDecimalFormat(floorDoubleToInt(lastReadingTime))
//    }

    /** Datum posledního čtení */
    val lastReadingDateText = book.lastReading?.date?.let { localDateTimeStringFromTimestamp(it) }

    /** Zjistí jestli čtenář stíhá dočíst knihu v nastaveném termínu */
    val gotIt = book.goalReadingSpeed?.let { goalSpeed ->
        readingSpeed?.let { readingSpeed ->
            readingSpeed >= goalSpeed
        }
    }

    /** Výpočet zbávajících stran pro dnešní den
     * ((dayOfReading * readingSpeed) - currentPage) * */
    val todayPageLeft = book.goalReadingSpeed?.let {
        (dayOfReading.toDouble() * it).minus(currentPage)
    }

    val todayPageLeftText = todayPageLeft?.floorDoubleToInt().toString()


    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.reading_info_header),
                style = MaterialTheme.typography.subtitle1,
            )

            Spacer(modifier = modifier.weight(1f))

            content()
        }

        if (visibleReadingBookShelf(book)) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {

                /** Dnes zvývá přečíst */
                todayPageLeft?.let {

                    val todayPageFinishText = currentPage.plus(it).floorDoubleToInt()

                    MyDoubleTextRow(
                        label = stringResource(R.string.today_page_left_header),
                        text = buildAnnotatedString {
                            append(todayPageLeftText.inflectionPage())

                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp)
                            ) {
                                append("    (tj. do strany ${todayPageFinishText})")
                            }
                        }
                    )
                    Divider(thickness = 0.5.dp, modifier = modifier.padding(vertical = 4.dp))

                }

                /** Počet čtecích dní */
                MyDoubleTextRow(
                    label = stringResource(R.string.reading_day_count_header),
                    text = dayOfReading.inflectionDay()
                )

                /** Poslední čtení - datum */
                lastReadingTime?.let {
                    Divider(thickness = 0.5.dp, modifier = modifier.padding(vertical = 4.dp))
                    MyDoubleTextRow(
                        label = stringResource(R.string.last_reading_header),
                        text = buildAnnotatedString {
                            append(lastReadingDateText ?: "")

                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            ) {
                                append("    (${it.inflectionDay2()})")
                            }
                        }
                    )
                }

                /** Poslední čtení - datum */
                book.lastReading?.pageRangeList?.let {
                    MyDoubleTextRow(
                        label = stringResource(R.string.last_reading_pages_header),
                        text = "${getPagesFromPageRangeList(it)?.inflectionPage()}"
                    )
                }

                /** Rychlost čtení */
                readingSpeed?.let {
                    val labelText =
                        if (book.bookShelf == BookShelf.Done.toString()) stringResource(R.string.reading_end_speed_header)
                        else stringResource(R.string.reading_speed_header)
                    Divider(thickness = 0.5.dp, modifier = modifier.padding(vertical = 4.dp))
                    MyDoubleTextRow(
                        label = labelText,
                        text = "${it.inflectionPage()} / den"
                    )
                }

                /** Odhadované dočtení */
                //            if (!readingDone)
                estimateFinishedDays?.let {
                    val labelText = when (gotIt) {
                        true -> "${stringResource(R.string.estimated_reading_time_header)}   \uD83D\uDC4D"
                        false -> "${stringResource(R.string.estimated_reading_time_header)}   \uD83D\uDC4E"
                        else -> stringResource(R.string.estimated_reading_time_header)
                    }

                    MyDoubleTextRow(
                        label = labelText,
                        text = buildAnnotatedString {
                            append(estimatedDateText)

                            if (estimatedDateText != Constants.infinityString) {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                ) {
                                    append("    (${it.inflectionDay()})")
                                }
                            }
                        }
                    )
                }

                /** Cíl čtení */
                if (book.bookShelf != BookShelf.Done.toString()) {
                    book.goalReadingSpeed?.let {
                        Divider(thickness = 0.5.dp, modifier = modifier.padding(vertical = 4.dp))

                        /** Cíl čtení - požadovaná rychlost čtení - počet stran za den */
                        MyDoubleTextRow(
                            label = stringResource(R.string.reading_speed_goal_header),
                            text = "${it.floorDoubleToInt()} stran / den"
                        )

                        /** Cíl dočtení */
                        MyDoubleTextRow(
                            label = stringResource(R.string.reading_time_goal_header),
                            text = buildAnnotatedString {
                                append(goalFinishedDateText ?: Constants.infinityString)

                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                ) {
                                    append("    (${goalFinishedDays?.inflectionDay()})")
                                }
                            }
                        )

                        Text(
                            modifier = Modifier.alpha(0.75f),
                            text = "(Při dodržení požadované rychlosti čtení v následujících dnech)",
                            style = MaterialTheme.typography.caption.copy(fontSize = 6.sp),
                        )
                    }
                }
            }
        }
    }

}