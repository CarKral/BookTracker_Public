package com.example.book_tracker.core.presentation.components.reorderable_list

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.book_tracker.core.domain.model.book.Book
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun ReorderableLazyColumn(
    modifier: Modifier = Modifier,
    items: List<Book>,
    onMove: (Int, Int) -> Unit,
    onDragEnd: () -> Unit,
    content: @Composable (offset: Float?, item: Any) -> Unit,
) {

    val scope = rememberCoroutineScope()
    var overScrollJob by remember { mutableStateOf<Job?>(null) }
    val reorderableListState = rememberReorderableListState(onMove = onMove)

    LazyColumn(
        state = reorderableListState.lazyListState,
        modifier = Modifier.pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDrag = { change, offset ->
                    change.consume()
                    reorderableListState.onDrag(offset = offset)

                    if (overScrollJob?.isActive == true) return@detectDragGesturesAfterLongPress

                    reorderableListState
                        .checkForOverScroll()
                        .takeIf { it != 0f }
                        ?.let {
                            overScrollJob = scope.launch {
                                reorderableListState.lazyListState.scrollBy(it)
                            }
                        } ?: kotlin.run { overScrollJob?.cancel() }
                },
                onDragStart = { offset -> reorderableListState.onDragStart(offset) },
                onDragEnd = {
                    reorderableListState.onDragInterrupted()
                    onDragEnd()
                },
                onDragCancel = { reorderableListState.onDragInterrupted() },

                )
        },
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(items) { index, item ->
            content(
                offset = reorderableListState.elementDisplacement.takeIf {
                    index == reorderableListState.currentIndexOfDraggedItem
                },
                item = item
            )
        }
    }
}
