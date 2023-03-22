package com.example.book_tracker.features.feature_book.presentation._components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.PopupProperties
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.features.feature_book.presentation.util.visibleReadingBookShelf
import com.example.book_tracker.core.presentation.components.TextButtonWithIcon

@Composable
fun ReadingSettingSection(
    modifier: Modifier = Modifier,
    book: Book,
    onResetReading: () -> Unit,
    onSetReadingGoal: () -> Unit,
    onShowReadingList: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {

        val context = LocalContext.current
        var readingMenuExpanded by remember { mutableStateOf(false) }

        Spacer(modifier = modifier.weight(1f))
        Box {

            TextButtonWithIcon(
                text = stringResource(R.string.reading_settings),
                enabled = visibleReadingBookShelf(book),
                imageVector = Icons.Filled.ArrowDropDown,
                onClick = { readingMenuExpanded = true }
            )

            DropdownMenu(
                properties = PopupProperties(
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true,
                ),
                expanded = readingMenuExpanded,
                onDismissRequest = { readingMenuExpanded = false }) {

                DropdownMenuItem(
                    onClick = {
                        onSetReadingGoal()
                        readingMenuExpanded = false
                    },
                ) {
                    Text(text = stringResource(R.string.reading_goal_set))
                }
                DropdownMenuItem(
                    onClick = {
                        onShowReadingList()
                        readingMenuExpanded = false
                    },
                ) {
                    Text(text = stringResource(R.string.readings_list))
                }
                DropdownMenuItem(
                    onClick = {
                        onResetReading()
                        readingMenuExpanded = false
                    },
                ) {
                    Text(text = stringResource(R.string.reading_goal_reset))
                }
            }
        }
    }
}