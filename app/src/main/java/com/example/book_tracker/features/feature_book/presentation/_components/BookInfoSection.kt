package com.example.book_tracker.features.feature_book.presentation._components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book


@Composable
fun BookInfoSection(
    modifier: Modifier = Modifier,
    book: Book,
    isItMe: Boolean,
) {

    val isItMyBook = book.userId == book.creatorId
    val hasGoogleBook = book.googleBookId != null

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val labelText = MaterialTheme.typography.body2
        val contentText = MaterialTheme.typography.body1

        Column(
            modifier = modifier.padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = stringResource(R.string.page_count), style = labelText)
            Text(
                modifier = modifier.padding(8.dp),
                text = book.pageCount.toString(), style = contentText
            )
        }
        book.publishedDate?.let {
            Column(
                modifier = modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = stringResource(R.string.published), style = labelText)
                Text(
                    modifier = modifier.padding(8.dp),
                    text = it/*.slice(0..3)*/, style = contentText
                )
            }
        }
        book.rating?.let {
            Column(
                modifier = modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = stringResource(R.string.rating), style = labelText)
                Text(
                    modifier = modifier.padding(8.dp),
                    text = it.toString(), style = contentText
                )
            }
        }
        if (!hasGoogleBook) {
            Column(
                modifier = modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = stringResource(R.string.created), style = labelText)
                Text(
                    modifier = modifier.padding(8.dp),
                    text = if (isItMyBook) {
                        if (isItMe) "JÁÁ"
                        else "ON"
                    } else "NĚKDO", style = contentText
                )
                Text(
                    text = "${book.creatorId}", style = MaterialTheme.typography.overline
                )
            }
        }
    }
}