package com.example.book_tracker.features.feature_reading_list.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.book_tracker.core.domain.model.book.BookReading
import com.example.book_tracker.core.presentation.util.floorDoubleToInt
import com.example.book_tracker.core.presentation.util.localDateTimeStringFromTimestamp
import com.example.book_tracker.features.feature_book.presentation._components.getPagesFromPageRangeList

@Composable
fun ReadingItem(
    modifier: Modifier = Modifier,
    reading: BookReading,
    onReadingItemClick: () -> Unit,
//    onAddClick: (Item, Boolean) -> Unit,

) {

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(4.dp)
            .clip(MaterialTheme.shapes.small)
            .fillMaxWidth()
            .clickable { onReadingItemClick() },
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Text(
                modifier = modifier.weight(1f),
                text = localDateTimeStringFromTimestamp(reading.date) ?: "",
                style = MaterialTheme.typography.overline,
            )
            reading.pageRangeList?.let {
                Text(
                    modifier = modifier.weight(1f),
                    text = "${
                        getPagesFromPageRangeList(it)?.floorDoubleToInt()
                    } stran",
                    style = MaterialTheme.typography.body2,
                )

                Text(
                    modifier = modifier.weight(2f),
                    text = "Strany: ${"${it[0]} - ${it[1]}"}",
                    style = MaterialTheme.typography.body2,
                )

            }

        }
    }
}
