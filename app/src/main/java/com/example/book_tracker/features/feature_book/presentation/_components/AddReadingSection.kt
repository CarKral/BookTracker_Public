package com.example.book_tracker.features.feature_book.presentation._components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.book_tracker.R
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.presentation.util.floorDoubleToInt
import com.example.book_tracker.core.presentation.util.replaceCommaToDot
import kotlin.math.roundToInt

fun getNewPage(newPage: Double, addition: Boolean): Int {
    return if (addition) {
        if (newPage.roundToInt() == newPage.toInt()) newPage.roundToInt().plus(1)
        else newPage.roundToInt()
    } else {
        if (newPage.roundToInt() == newPage.toInt()) newPage.roundToInt().minus(1)
        else newPage.toInt()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddPageSection(
    modifier: Modifier = Modifier,
    book: Book,
    newPage: String?,
    onNewPageChanged: (String?) -> Unit,
    onAddNewReading: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    /** Použito pro tlačítka -/+ pro přidávání a odebírání přečtených stran. */
    val interactionSourceMinus = remember { MutableInteractionSource() }
    val isPressedMinus by interactionSourceMinus.collectIsPressedAsState()
    val interactionSourcePlus = remember { MutableInteractionSource() }
    val isPressedPlus by interactionSourcePlus.collectIsPressedAsState()

    /** Proměnná pro zjištění zda je umožněno přidávání přečtených stran. */
    var enabledAdding by remember { mutableStateOf(book.pageCount != null && book.pageCount != book.currentPage?.toInt()) }

    /** Proměnná pro zjištění zda je umožněno odebírání přečtených stran. */
    var enabledRemoving by remember { mutableStateOf(book.pageCount != null && book.currentPage != 0.0) }

    /** Proměnná pro zjištění zda uživatel zapisuje hodnotu přečtených stran ručně, nebo zda je hodnota přidávána pomocí tlačítek -/+.
     *  Při přidávání hodnoty ručně, zapsaná hodbnota se "neuzemňuje" (flooreDoubleToInt), při přídávání pomocí tlačítek ano.
     *  @see floorDoubleToInt()
     *  */
    var isTyping by remember { mutableStateOf(false) }

    val currentPage = book.currentPage ?: 0.0
    val pageCount = book.pageCount?.toDouble() ?: 0.0
    val newPageDouble = newPage?.toDoubleOrNull() ?: 0.0
    val newPagesText = newPage ?: ""

    if (newPageDouble == pageCount) enabledAdding = false

    fun hideKeyboard() {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    fun resetKeyboard() {
        isTyping = false
        hideKeyboard()
    }

    /** Odebírání přečtených stran */
    fun safeRemovePage() {
        resetKeyboard()
        val newPageInt = getNewPage(newPage = newPageDouble, addition = false)

        if (newPageInt >= 0.0) {
            onNewPageChanged(newPageInt.toString())
            enabledAdding = true
            if (newPageInt == 0) enabledRemoving = false
        } else enabledRemoving = false
    }

    /** Přidávání přečtených stran */
    fun safeAddPage() {
        resetKeyboard()
        val newPageInt = getNewPage(newPage = newPageDouble, addition = true)

        if (newPageInt <= pageCount) {
            onNewPageChanged(newPageInt.toString())
            enabledRemoving = true
            if (newPageInt == pageCount.toInt()) enabledAdding = false
        } else enabledAdding = false
    }

    /** Zapsání nově přidáné strany */
    fun setNewPages(value: String?) {
        isTyping = true
        value?.let {
            val number = it.replaceCommaToDot()
            if (number.isEmpty()) onNewPageChanged("")
            if (number.toDoubleOrNull() != null) {
                onNewPageChanged(number)
            }
        }
    }

    fun onAdd() {
        resetKeyboard()
        onAddNewReading()
    }

    if (isPressedMinus) safeRemovePage()
    if (isPressedPlus) safeAddPage()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Spacer(modifier = modifier.weight(1f))

        Row(
            modifier = modifier
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            IconButton(
                enabled = enabledRemoving,
                onClick = { safeRemovePage() },
                interactionSource = interactionSourceMinus
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_remove_24),
                    tint = if (enabledRemoving) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary,
                    contentDescription = "Page remove"
                )
            }

            Column() {
                OutlinedTextField(
                    modifier = modifier
                        .width(100.dp)
                        .padding(horizontal = 4.dp),
                    value = if (isTyping) newPagesText else newPageDouble.floorDoubleToInt().toString(),
//                        value = myBook.currentPage.toString(),
                    onValueChange = { if (it.count() <= 6) setNewPages(it) },
                    label = { Text(text = "Strana") },
                    singleLine = true,
                    maxLines = 1,
                    isError = newPagesText.isEmpty() || newPageDouble > pageCount,
                    textStyle = MaterialTheme.typography.button.copy(textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onAdd() })
                )
                AnimatedVisibility(visible = newPagesText.isEmpty() || newPageDouble > pageCount) {
                    Text(
                        text = if (newPagesText.isEmpty()) stringResource(R.string.add_reading_empty_field)
                        else if ((newPageDouble) > pageCount) stringResource(R.string.add_reading_to_much_pages) else "",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }

            IconButton(
                enabled = enabledAdding,
                onClick = { safeAddPage() },
                interactionSource = interactionSourcePlus
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_add_24),
                    tint = if (enabledAdding) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary,
                    contentDescription = "Page add"
                )
            }
        }

        Spacer(modifier = modifier.weight(1f))

        TextButton(
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(8.dp),
            enabled = newPagesText.isNotEmpty() && newPageDouble <= pageCount && newPageDouble != currentPage,
            onClick = { onAdd() }
        )
        {
            Text(
                modifier = modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                text = stringResource(R.string.add_reading),
                style = MaterialTheme.typography.button
            )
        }
    }
}