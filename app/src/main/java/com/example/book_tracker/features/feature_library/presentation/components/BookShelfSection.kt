package com.example.book_tracker.features.feature_library.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.features.feature_book_list.presentation.components.BookItemLibrary


/** BookShelf section
 * - Obsahuje header pro název poličky a LazyRow (BooksList) pro knihy
 * - použito na LibraryScreen, UserLibraryScreen
 */
@Composable
fun BookShelfSection(
//    bookShelf: BookShelf,
    bookShelf: String,
    books: List<Book>,
    onMoreBookClick: () -> Unit,
    onBookItemClick: (String?) -> Unit,
) {
//    val bookShelfString = bookShelf.getLabel(LocalContext.current)

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "$bookShelf:",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 2.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                modifier = Modifier.padding(end = 4.dp),
                onClick = onMoreBookClick,
                enabled = books.isNotEmpty() && (books.size > 1)
//                modifier = Modifier.clip(RoundedCornerShape(50.dp))
            ) {
                Text(text = stringResource(R.string.give_me_more))
            }
        }

//        if (books.isEmpty()) {
//            Text(
//                modifier = Modifier
//                    .padding(top = 0.dp, bottom = 16.dp)
//                    .fillMaxWidth(),
//                text = stringResource(R.string.no_books),
//                style = MaterialTheme.typography.body1,
//                textAlign = TextAlign.Center
//            )
//        } else {
        BookList(books = books, onBookItemClick = onBookItemClick)
//        }

        Divider(
            thickness = 2.dp,
            modifier = Modifier.padding(top = 16.dp, start = 4.dp, end = 4.dp),
        )
    }
}


@Composable
private fun BookList(
    modifier: Modifier = Modifier,
//    bookShelf: BookShelf,
    books: List<Book>,
    onBookItemClick: (String?) -> Unit

) {

    LazyRow(
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(books) {
            BookItemLibrary(
                book = it,
                onBookItemClick = { onBookItemClick(it.id) },
            )
        }
        if (books.size > 1) {
            item {
                Text(
                    modifier = modifier.padding(8.dp),
                    text = "↑↑↑ ↑↑↑\n\nPro více knih,\nklikni výše",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}