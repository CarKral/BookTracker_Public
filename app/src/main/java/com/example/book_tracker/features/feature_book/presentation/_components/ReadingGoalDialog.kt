package com.example.book_tracker.features.feature_book.presentation._components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.placeholderBook
import com.example.book_tracker.core.presentation.util.*

@Composable
fun ReadingGoalDialog(
    book: Book?,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onConfirm: (goalSpeed: Double?) -> Unit,
) {
    //* Zbývající počet stran k dočtení pro vypočítání cíle */
    val pages = book?.pageCount?.toDouble()?.minus(book.currentPage ?: 0.0) ?: 0.0
    val speed = book?.goalReadingSpeed?: 20.0

    val finishedDays = calculateGoalFinishedDays(pages, speed)
    val finishedDate = calculateGoalFinishedDate(finishedDays)

    var goalSpeed by remember { mutableStateOf(speed) }
    var goalSpeedText by remember { mutableStateOf(speed.floorDoubleToInt().toString()) }
    var goalFinishedDays by remember { mutableStateOf(finishedDays) }
    var goalFinishedText by remember { mutableStateOf(finishedDays.floorDoubleToInt().toString()) }

    var goalFinishedDate by remember { mutableStateOf(finishedDate) }

    //* Zjistí, zda je umožněno uložení cíle */
    val enabledSaveButton = goalSpeedText.isNotEmpty() && goalSpeed != Double.POSITIVE_INFINITY &&
            goalFinishedText.isNotEmpty() && goalFinishedDays != Double.POSITIVE_INFINITY

    //* Datum odhadovaného dočtení knihy */
    val goalFinishedDateText =
        if (goalSpeed != 0.0 && goalFinishedDays != 0.0) {
            localDateTimeStringFromTimestamp(goalFinishedDate) ?: Constants.infinityString
        } else Constants.infinityString

    //* Zapíše hodnoty pro cílovou rychlost čtení */
    fun setNewGoalSpeed(value: String?) {
        value?.let {
            val number = it.replaceCommaToDot()
            if (number.isEmpty()) goalSpeedText = number
            else if (number.toDoubleOrNull() == 0.0) goalSpeedText = number
            else {
                goalSpeedText = when (number.toDoubleOrNull()) {
                    null -> goalSpeedText
                    else -> number
                }
                if (goalSpeedText.isNotEmpty()) {
                    goalSpeed = goalSpeedText.toDouble()
                    goalFinishedDays = pages.div(goalSpeed)
                    goalFinishedText = goalFinishedDays.floorDoubleToInt().toString()
                    goalFinishedDate = calculateGoalFinishedDate(goalFinishedDays)
                }
            }
        }
    }

    //* Zapíše hodnoty pro cílový počet dní čtení */
    fun setNewGoalFinished(value: String?) {
        value?.let {
            val number = it.replaceCommaToDot()
            if (number.isEmpty()) goalFinishedText = number
            else if (number.toDoubleOrNull() == 0.0) goalFinishedText = number
            else {
                goalFinishedText = when (number.toDoubleOrNull()) {
                    null -> goalFinishedText
                    else -> number
                }
                if (goalFinishedText.isNotEmpty()) {
                    goalFinishedDays = goalFinishedText.toDouble()
                    goalSpeed = pages.div(goalFinishedDays)
                    goalSpeedText = goalSpeed.floorDoubleToInt().toString()
                    goalFinishedDate = calculateGoalFinishedDate(goalFinishedDays)
                }
            }
        }
    }

    fun onConfirmed() = onConfirm(goalSpeed.roundTo(2))

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 22.dp)
                        .align(Alignment.Start),
                    text = stringResource(R.string.reading_goal_header), style = MaterialTheme.typography.subtitle1
                )

                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.reading_goal_day_pages),
                        style = MaterialTheme.typography.body2
                    )
                    OutlinedTextField(
                        modifier = Modifier.width(80.dp),
                        value = goalSpeedText,
                        onValueChange = { if (it.count() <= 5) setNewGoalSpeed(it) },
                        singleLine = true,
                        maxLines = 1,
                        isError = goalSpeedText.isZeroOrEmpty(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.reading_goal_finish_in_days),
                            style = MaterialTheme.typography.body2
                        )
                        Text(
                            modifier = Modifier
                                .alpha(0.75f)
                                .padding(top = 4.dp),
                            text = "· Včetně dnešního dne",
                            style = MaterialTheme.typography.caption.copy(fontSize = 6.sp),
                        )
                        Text(
                            modifier = Modifier
                                .alpha(0.75f)
                                .padding(top = 4.dp, end = 16.dp),
                            text = "· POZOR: Počet dní k dočtení se v průběhu čtení může změnit.",
                            style = MaterialTheme.typography.caption.copy(fontSize = 6.sp),
                        )

                    }
//                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedTextField(
                        modifier = Modifier.width(80.dp),
                        value = goalFinishedText,
                        onValueChange = { if (it.count() <= 5) setNewGoalFinished(it) },
                        singleLine = true,
                        maxLines = 1,
                        isError = goalFinishedText.isZeroOrEmpty(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { onConfirmed() })
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider(thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.Start)
                        .alpha(0.75f),
                    text = "\uD83D\uDCD6    Zbývá ti přečíst: ${pages.toInt()} stran",
                    style = MaterialTheme.typography.overline,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .align(Alignment.Start)
                        .alpha(0.75f),
                    text = "\uD83D\uDCC5    Budeš mít dočteno: $goalFinishedDateText",
                    style = MaterialTheme.typography.overline,
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    TextButton(
                        onClick = { onDelete() }) {
                        Text(
                            text = stringResource(R.string.delete), color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.button.copy(fontSize = 10.sp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = { onDismiss() }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        enabled = enabledSaveButton,
                        onClick = { onConfirmed() }) {
                        Text(text = stringResource(R.string.save_it))
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ResetReadingGoalDialogPreview() {
    ReadingGoalDialog(book = placeholderBook, onDismiss = { }, onConfirm = {}, onDelete = {})
}