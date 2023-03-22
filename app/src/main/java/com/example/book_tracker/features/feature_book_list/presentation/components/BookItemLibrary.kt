package com.example.book_tracker.features.feature_book_list.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookPrintType
import com.example.book_tracker.features.feature_book.presentation.util.visibleReadingBookShelf
import com.example.book_tracker.core.presentation.util.floorDoubleToInt
import com.example.book_tracker.core.presentation.util.localDateFromTimestamp
import com.example.book_tracker.core.presentation.util.localDateTimeStringFromTimestamp
import com.google.firebase.Timestamp

/**
 * BookItemLibrary is used as BookList item at BookShelfSection at LibraryScreen
 */
@Composable
fun BookItemLibrary(
    modifier: Modifier = Modifier,
    book: Book,
//    visibleProgressBar: Boolean = false,
    onBookItemClick: () -> Unit
) {

    val visibleProgressBar by remember {
        // Checks if current book is in the set of bookshelves for visible progress bar
        mutableStateOf(visibleReadingBookShelf(book))
    }

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
//            .padding(8.dp)
            .height(120.dp)
            .width(250.dp)
//            .defaultMinSize(minHeight = 100.dp, minWidth = 250.dp)
//            .height(IntrinsicSize.Max)
//            .width(IntrinsicSize.Max)
            .clip(MaterialTheme.shapes.small)
            .clickable { onBookItemClick() },
//        border = BorderStroke(0.1.dp, MaterialTheme.colors.primary)
    ) {

        Box {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(bottom = if (visibleProgressBar) 5.dp else 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                ThumbnailSection(book = book)

                TextSection(book = book)
            }

            if (visibleProgressBar) {
                PageCountProgressBar(
                    modifier = modifier.align(Alignment.BottomCenter),
                    book = book
                )
            }
        }
    }
}

@Composable
private fun ThumbnailSection(
    modifier: Modifier = Modifier,
    book: Book
) {
    if (book.photoUrl != null) {
        AsyncImage(
            placeholder = painterResource(R.drawable.book_cover_placeholder),
            model = book.photoUrl,
            modifier = Modifier.fillMaxHeight(),
            contentDescription = "Book Cover Image",
        )
    } else {
        Image(
            painter = painterResource(R.drawable.book_cover_placeholder),
            contentDescription = "Book Cover Image",
            modifier = Modifier.height(120.dp),
            contentScale = ContentScale.FillHeight,
        )
    }
}

@Composable
private fun TextSection(
    modifier: Modifier = Modifier,
    book: Book
) {
    val context = LocalContext.current

    Box {
        Column(
            modifier.padding(start = 6.dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
        ) {

            book.title?.let {
                Text(
                    text = it,
                    modifier.padding(bottom = 5.dp),
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            book.authors?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Divider(thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))

            when (book.bookShelf) {
                BookShelf.CurrentRead.toString() -> {
                    StartReadingDate(book.startedReading)
                    PageReadText(book.pageCount, book.currentPage)
                }
                BookShelf.Done.toString() -> {
                    StartReadingDate(
                        startedReading = book.startedReading,
                        finishedReading = book.finishedReading
                    )
                    //                            FinishedReadingDate(book.finishedReading)
                    PageReadText(book.pageCount, book.currentPage)
                }
                BookShelf.Unfinished.toString() -> {
                    StartReadingDate(book.startedReading)
                    book.pageCount?.let { MyText(text = "$it stran") }
                }
                else -> {
                    book.pageCount?.let { MyText(text = "$it stran") }
                }
            }


        }
        book.printType?.let { printTypes ->
            Row(
                modifier = modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Spacer(modifier = Modifier.weight(1f))
                for (type in BookPrintType.values()) {
                    if (printTypes.contains(type.toString()))

                        Icon(
                            modifier = modifier
                                .padding(end = 4.dp)
                                .size(16.dp),
                            painter = painterResource(BookPrintType.valueOf(type.toString()).getIcon()),
                            contentDescription = "icon"
                        )


                    //                        MyText(
                    //                            modifier = Modifier.padding(end = 4.dp),
                    //                            text = "${type.getLabel(context)} ✅",
                    //                            style = MaterialTheme.typography.caption.copy(fontSize = 8.sp)
                    ////                            textAlign = TextAlign.End,
                    //                        )
                }
            }
        }
    }
}

@Composable
private fun StartReadingDate(startedReading: Timestamp?, finishedReading: Timestamp? = null) {
    if (startedReading != null) {
        val startDateText = localDateTimeStringFromTimestamp(startedReading)

        if (finishedReading == null) {
            MyText(text = "Čteno od:    $startDateText")
        } else {
            val startDate = localDateFromTimestamp(startedReading)
            val finishedDate = localDateFromTimestamp(finishedReading)
            val finishedDateText = localDateTimeStringFromTimestamp(finishedReading)

            val date = if (startDate.year == finishedDate.year) {
                "${startDate.dayOfMonth}.${startDate.monthValue}. - " +
                        "${finishedDate.dayOfMonth}.${finishedDate.monthValue}.${finishedDate.year}"
            } else "$startDateText - $finishedDateText"

            MyText(text = "Čteno od:    $date")
        }
    }
}

@Composable
private fun PageReadText(pageCount: Int?, currentPage: Double?) {
    pageCount?.let {
        val flooredCurrentPage = currentPage?.floorDoubleToInt() ?: 0
        MyText(text = "Přečteno:    ${flooredCurrentPage}/${pageCount} stran")
    }
}

@Composable
private fun MyText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.caption.copy(fontSize = 10.sp),
    maxLines: Int = 1
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun PageCountProgressBar(
    book: Book,
    modifier: Modifier = Modifier,
) {
    val pageCount = book.pageCount
    if (pageCount == null) return
    else {
        val currentPage = book.currentPage ?: 0.0

//            if (book.bookShelf == BookShelf.Done.toString()) pageCount
//            else book.currentPage ?: 0

        LinearProgressIndicator(
            progress = currentPage.toFloat() / pageCount.toFloat(),
            modifier
                .fillMaxWidth()
                .height(5.dp)
//                    color = Color.Green,
//                    trackColor = Color.Gray
        )
    }
}