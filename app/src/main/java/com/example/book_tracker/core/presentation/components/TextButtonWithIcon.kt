package com.example.book_tracker.core.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun TextButtonWithIcon(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    TextButton(
        contentPadding = PaddingValues(),
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
    )
    {
        Row(
            modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier.padding(4.dp),
                text = text,
                style = MaterialTheme.typography.button
            )
            Icon(imageVector, contentDescription = text)
        }
    }
}
