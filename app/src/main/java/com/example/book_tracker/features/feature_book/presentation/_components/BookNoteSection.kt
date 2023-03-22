package com.example.book_tracker.features.feature_book.presentation._components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.book_tracker.R


@Composable
fun BookNoteSection(
    modifier: Modifier = Modifier,
    note: String,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {

        Text(text = stringResource(R.string.public_note_section), style = MaterialTheme.typography.subtitle1)
        Text(
            modifier = modifier.padding(8.dp),
            text = note, style = MaterialTheme.typography.caption
        )
    }
}