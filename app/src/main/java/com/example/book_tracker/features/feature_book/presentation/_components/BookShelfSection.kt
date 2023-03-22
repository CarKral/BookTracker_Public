package com.example.book_tracker.features.feature_book.presentation._components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.placeholderBook
import com.example.book_tracker.core.presentation.components.MyDoubleTextRow
import com.example.book_tracker.core.presentation.util.localDateTimeStringFromTimestamp

@Composable
fun BookShelfSection(
    modifier: Modifier = Modifier,
    book: Book,
    content: @Composable () -> Unit
) {
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
                text = stringResource(R.string.bookshelf_info_header),
                style = MaterialTheme.typography.subtitle1,
            )

            Spacer(modifier = modifier.weight(1f))

            content()
        }

        Column(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {

            book.addedDate?.let {
                MyDoubleTextRow(
                    label = stringResource(R.string.book_added_header),
                    text = localDateTimeStringFromTimestamp(it) ?: ""
                )
            }
            book.startedReading?.let {
                MyDoubleTextRow(
                    label = stringResource(R.string.book_started_header),
                    text = localDateTimeStringFromTimestamp(it) ?: ""
                )
            }
            book.finishedReading?.let {
                MyDoubleTextRow(
                    label = stringResource(R.string.book_finished_header),
                    text = localDateTimeStringFromTimestamp(it) ?: ""
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BookShelfSectionPreview() {
    BookShelfSection(book = placeholderBook, content = {})
}