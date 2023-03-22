package com.example.book_tracker.features.feature_note_list.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.book_tracker.core.presentation.util.localDateTimeStringFromTimestamp
import com.example.book_tracker.core.domain.model.book.BookNote

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: BookNote,
    onNoteItemClick: () -> Unit,
//    onAddClick: (Item, Boolean) -> Unit,

) {

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .padding(4.dp)
            .clip(MaterialTheme.shapes.small)
            .fillMaxWidth()
            .clickable { onNoteItemClick() },
    ) {
        Row(
            modifier = modifier
//                .padding(0.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            note.date?.let {

                val date = localDateTimeStringFromTimestamp(it) ?: ""
                Column(
                    modifier.padding(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = date,
                        style = MaterialTheme.typography.overline,
                        textAlign = TextAlign.End
                    )

                    Text(
                        modifier = modifier.padding(4.dp),
                        text = note.text ?: "",
                        style = MaterialTheme.typography.body2,
                    )
                }
            }


        }
    }
}
