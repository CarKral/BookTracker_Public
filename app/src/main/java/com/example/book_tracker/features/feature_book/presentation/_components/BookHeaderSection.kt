package com.example.book_tracker.features.feature_book.presentation._components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.*
import com.example.book_tracker.core.presentation.util.floorDoubleToInt
import com.example.book_tracker.features.feature_book.presentation.util.visibleReadingBookShelf

enum class Borrowed(private val text: String) {
    TO("Zapůjčeno komu:"), FROM("Vypůjčeno od:");

    fun getText() = text
}

@Composable
fun BookHeaderSection(
    modifier: Modifier = Modifier,
    book: Book,
) {

    val context = LocalContext.current

    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            modifier = modifier
                .weight(2f)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = if (visibleReadingBookShelf(book)) Alignment.CenterHorizontally else Alignment.Start
        ) {

            ThumbnailSection(
                book = book
            )

            /** Checks if current book is in the set of bookshelves for visible progress bar */
            if (visibleReadingBookShelf(book)) {
                PageCountProgressSection(book)
            }
        }
        Column(
            modifier = modifier
                .padding(8.dp)
                .weight(3f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            BookTitleSection(
                book = book
            )
            Spacer(modifier = modifier.height(12.dp))
            Divider(thickness = 0.5.dp)
            Spacer(modifier = modifier.height(12.dp))

            if (!book.borrowedTo.isNullOrEmpty()) {
                BorrowedText(Borrowed.TO, book.borrowedTo.toString())
            }
            if (!book.borrowedFrom.isNullOrEmpty()) {
                BorrowedText(Borrowed.FROM, book.borrowedFrom.toString())
            }
            Spacer(modifier = modifier.height(12.dp))

            book.printType?.let { printTypes ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (type in BookPrintType.values()) {
                        if (printTypes.contains(type.toString())) {

                            Icon(
                                modifier = modifier
                                    .size(40.dp)
                                    .padding(end = 8.dp),
                                painter = painterResource(BookPrintType.valueOf(type.toString()).getIcon()),
                                contentDescription = "icon"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BorrowedText(borrowed: Borrowed, text: String) {
    Text(
        text = "${borrowed.getText()}    $text",
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.End,
    )
}

@Composable
fun PrintTypeText(printType: String) {
    Text(
        modifier = Modifier.padding(end = 4.dp),
        text = "$printType ✅",
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.End,
    )
}

@Composable
fun ThumbnailSection(modifier: Modifier = Modifier, book: Book) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
//            .height(200.dp)
            .shadow(elevation = 16.dp, clip = true)
            .clip(MaterialTheme.shapes.small)
    ) {
        if (book.photoUrl != null) {
            AsyncImage(
                placeholder = painterResource(R.drawable.book_cover_placeholder),
                model = book.photoUrl,
                contentScale = ContentScale.FillWidth,
                contentDescription = "Book Cover Image",
            )
        } else {
            Image(
                painter = painterResource(R.drawable.book_cover_placeholder),
                contentDescription = "Book Cover Image",
//                modifier = modifier.height(200.dp),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}

@Composable
fun PageCountProgressSection(book: Book, modifier: Modifier = Modifier) {
    val pageCount = book.pageCount
    if (pageCount == null) return
    else {
        val currentPage = book.currentPage ?: 0.0
        val flooredCurrentPage = currentPage.floorDoubleToInt()
        val flooredPageLeft = (pageCount.toDouble().minus(currentPage)).floorDoubleToInt()

        LinearProgressIndicator(
            progress = currentPage.toFloat() / pageCount.toFloat(),
            modifier
                .padding(vertical = 8.dp)
//                .fillMaxWidth()
//                .width(150.dp)
                .height(5.dp)
        )
        Column(
//            modifier = modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Přečteno $flooredCurrentPage/$pageCount stran",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center,
            )
            if (book.bookShelf != BookShelf.Done.toString()) {
                Text(
                    text = "(Zbývá $flooredPageLeft stran)",
                    style = MaterialTheme.typography.overline,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun BookTitleSection(
    modifier: Modifier = Modifier,
    book: Book
) {
    book.title?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),
        )
    }
    Spacer(modifier.height(12.dp))

    book.subtitle?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.subtitle2,
        )
    }

    Spacer(modifier.height(12.dp))

    book.authors?.let {
        Text(
            text =
            if (book.publisher == null) it
            else "$it / ${book.publisher}",
            style = MaterialTheme.typography.body2,
        )
    }

    Spacer(modifier.height(12.dp))

    book.categories?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.body2,
        )
    }
}



@Preview(showBackground = true)
@Composable
fun BookHeaderSectionPreview() {
    BookHeaderSection(book = placeholderBook)
}