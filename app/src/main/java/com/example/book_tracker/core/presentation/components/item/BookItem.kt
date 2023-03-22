package com.example.book_tracker.core.presentation.components.item

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
import coil.compose.AsyncImage
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.model.book.BookPrintType
import com.example.book_tracker.features.feature_book.presentation.util.visibleReadingBookShelf
import com.example.book_tracker.core.presentation.util.floorDoubleToInt
import com.example.book_tracker.core.presentation.util.localDateTimeStringFromTimestamp
import com.google.firebase.Timestamp


/**
 * BookItem is used as BookList item at BookListScreen, HomeScreen,
 */
@Composable
fun BookItem(
    modifier: Modifier = Modifier,
    book: Book,
    onBookItemClick: () -> Unit
//    offset: Float? = null,
//    visibleProgressBar: Boolean = true,

) {

//    val isDragged = offset != null
    // Checks if current book is in the set of bookshelves for visible progress bar
    val visibleProgressBar by remember {
        mutableStateOf(visibleReadingBookShelf(book))
    }

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
//            .graphicsLayer {
//                translationY = offset ?: 0f
//            }
//            .zIndex(if (isDragged) 1f else 0f)
//            .padding(4.dp)
            .height(175.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { onBookItemClick() },
        backgroundColor = MaterialTheme.colors.surface
    ) {

        Box {
            Row(
                modifier
                    .padding(bottom = if (visibleProgressBar) 5.dp else 0.dp),
                verticalAlignment = Alignment.Top,
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
private fun PageCountProgressBar(
    book: Book,
    modifier: Modifier = Modifier,
) {
    val pageCount = book.pageCount
    if (pageCount == null) return
    else {
        val currentPage = book.currentPage ?: 0
//            if (book.bookShelf == BookShelf.Done.toString()) pageCount
//            else book.currentPage ?: 0

        LinearProgressIndicator(
            progress = currentPage.toFloat() / pageCount.toFloat(),
            modifier
                .height(5.dp)
                .fillMaxWidth(),
//                    color = Color.Green,
//                    trackColor = Color.Gray
        )
    }
}

@Composable
private fun ThumbnailSection(book: Book) {
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
            modifier = Modifier.height(175.dp),
            contentScale = ContentScale.FillHeight,
        )
    }
}

@Composable
private fun TextSection(
    modifier: Modifier = Modifier,
    book: Book,
) {

    val context = LocalContext.current

    Box {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 6.dp, end = 6.dp, bottom = 6.dp),

            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {

            book.title?.let {
                Text(
                    modifier = modifier.padding(bottom = 5.dp),
                    text = it,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Medium),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            //        book.subtitle?.let {
            //            MyText(text = it, style = MaterialTheme.typography.body2, maxLines = 2)
            //        }

            Spacer(modifier = Modifier.weight(4f))

            book.authors?.let {
                MyText(text = it, style = MaterialTheme.typography.body2)
            }

            Spacer(modifier = Modifier.weight(1f))

            book.publisher?.let {
                MyText(text = it)
            }

            Spacer(modifier = Modifier.weight(1f))

            book.categories?.let {
                MyText(text = it)
            }


            Spacer(modifier = Modifier.weight(2f))

            when (book.bookShelf) {
                BookShelf.CurrentRead.toString() -> {
                    StartReadingDate(book.startedReading)
                    PageReadText(book.pageCount, book.currentPage)
                }
                BookShelf.Done.toString() -> {
                    StartReadingDate(book.startedReading)
                    FinishedReadingDate(book.finishedReading)
                    PageReadText(book.pageCount, book.currentPage)
                }
                BookShelf.Unfinished.toString() -> {
                    StartReadingDate(book.startedReading)
                    PageReadText(book.pageCount, book.currentPage)
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
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Spacer(modifier = Modifier.weight(1f))
                for (type in BookPrintType.values()) {
                    if (printTypes.contains(type.toString()))

                        Icon(
                            modifier = modifier
                                .padding(end = 6.dp)
                                .size(20.dp),
                            painter = painterResource(BookPrintType.valueOf(type.toString()).getIcon()),
                            contentDescription = "icon"
                        )
                }
            }
        }
    }
}

@Composable
private fun StartReadingDate(startedReading: Timestamp?) {
    startedReading?.let {
        val date = localDateTimeStringFromTimestamp(it)
        MyText(text = "Čteno od:    $date")
    }
}

@Composable
private fun FinishedReadingDate(finishedReading: Timestamp?) {
    finishedReading?.let {
        val date = localDateTimeStringFromTimestamp(it)
        MyText(text = "Dočteno:     $date")
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
    style: TextStyle = MaterialTheme.typography.caption,
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