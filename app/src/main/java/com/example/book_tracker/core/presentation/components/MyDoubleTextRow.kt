package com.example.book_tracker.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun MyDoubleTextRowPreview() {
    MyDoubleTextRow(label = "Preview label", text = "Preview text ")
}


/**
 * Reusable Row with two Text elements inside.
 * One Text for label, the other one for text.
 * Example of use:  MyDoubleTextRow(label = "example label", text = "example text")
 * */
@Composable
fun MyDoubleTextRow(
    modifier: Modifier = Modifier,
    label: String,
    labelStyle: TextStyle = MaterialTheme.typography.caption,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.body2
) {
    Row(
        modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            modifier = modifier.weight(1f),
            style = labelStyle,
        )
        Text(
            text = text,
            modifier = modifier.weight(1f),
            style = textStyle,
        )
    }
}

@Composable
fun MyDoubleTextRow(
    modifier: Modifier = Modifier,
    label: String,
    labelStyle: TextStyle = MaterialTheme.typography.caption,
    text: AnnotatedString,
    textStyle: TextStyle = MaterialTheme.typography.body2
) {
    Row(
        modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            modifier = modifier.weight(1f),
            style = labelStyle,
        )
        Text(
            text = text,
            modifier = modifier.weight(1f),
            style = textStyle,
        )
    }
}