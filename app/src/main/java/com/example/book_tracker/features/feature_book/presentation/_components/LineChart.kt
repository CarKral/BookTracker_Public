package com.example.book_tracker.features.feature_book.presentation._components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookReading
import kotlin.math.roundToInt

fun getChartValues(list: List<BookReading>?): List<Offset>? {
    println("LIST of READINGS: " + list.toString())

    val points = mutableListOf<Offset>()

    if (list == null) return null

    val sortedList = list.sortedBy {
        it.date
    }

    sortedList.mapIndexed { index, item ->
        item.pageRangeList?.let { range ->
            println("RANGE LIST: " + range.toString())

            var x: Float
            var y: Float

            if (!range.contains(null) && range.isNotEmpty()) {
                item.date?.let {
//                    x = localDateFromTimestamp(it).dayOfMonth.toFloat()

                    x = index.plus(1).toFloat()

                    if (points.isEmpty()) {
                        range[1]?.let { it ->
                            points.add(Offset(x = 0f, y = 0f))
                            points.add(Offset(x = x, y = it.toFloat()))
                        }
                    } else {
                        range[1]?.let {
                            points.add(Offset(x = x, y = it.toFloat()))
                        }
                    }
                }
            }
        }
    }

    return points
}

@Preview(showBackground = true, backgroundColor = 0)
@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    book: Book = Book(pageCount = 666),
    totalDays: Double? = 0.0,
    goal: Double? = 30.0,
    list: List<Offset> = listOf(
        Offset(0f, 9f),
        Offset(1f, 10f),
        Offset(2f, 20f),
        Offset(3f, 21f),
        Offset(4f, 35f),
        Offset(5f, 45f),
        Offset(6f, 45f),
        Offset(7f, 55f),
        Offset(8f, 66f),
        Offset(9f, 166f),
    ),
) {
//    values.toMutableList().add(Offset(0f, book.pageCount!!.toFloat()))
    val goalDays = goal?.let { book.pageCount?.div(it)?.toFloat() } ?: 0f
    val values = list.toMutableList()

//    values.add(Offset(goalDays, book.pageCount?.toFloat() ?: 0f))
    values.add(Offset(0f, book.pageCount?.toFloat() ?: 0f))

    val minXValue = values.minOf { it.x }
    val maxXValue = values.maxOf { it.x }
    val minYValue = values.minOf { it.y }
    val maxYValue = values.maxOf { it.y }

    Row(
        Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(15.dp)
            .padding(start = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        var offsetPoints by remember { mutableStateOf<List<Offset>?>(emptyList()) }

        val paint = Paint().asFrameworkPaint()
        paint.color = 0xFFFFFFFF.toInt()
        paint.textAlign = android.graphics.Paint.Align.CENTER
        paint.textSize = 12f

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .drawBehind {

                    val canvasHeight = size.height
                    val canvasWidth = size.width

                    val dotSize = 3f
                    val lineWidth = 2f
                    val borderLineWidth = 0.4f

                    val path = Path()

                    // Border points
                    val borderPoints = listOf(
                        // TOP-LEFT -> TOP-RIGHT
                        Pair(Offset(0f, 0f), Offset(canvasWidth, 0f)),
                        // TOP-RIGHT -> BOTTOM-RIGHT
                        Pair(Offset(canvasWidth, 0f), Offset(canvasWidth, canvasHeight)),
                        // BOTTOM-RIGHT -> BOTTOM-LEFT
                        Pair(Offset(canvasWidth, canvasHeight), Offset(0f, canvasHeight)),
                        // BOTTOM-LEFT -> TOP-LEFT
                        Pair(Offset(0f, canvasHeight), Offset(0f, 0f)),
                    )

                    // Border lines
                    for (point in borderPoints) {
                        drawLine(
                            start = point.first,
                            end = point.second,
                            strokeWidth = borderLineWidth,
                            color = Color.LightGray
                        )
                    }

                    offsetPoints = values.map {

                        val x = it.x.mapToRange(
                            inMin = minXValue,
                            inMax = maxXValue,
                            outMin = 0f,
                            outMax = size.width
                        )
                        val y = it.y.mapToRange(
                            inMin = minYValue,
                            inMax = maxYValue,
                            outMin = size.height,
                            outMax = 0f
                        )

                        Offset(x, y)
                    }

                    offsetPoints?.let {
                        it.forEachIndexed { index, offset ->
                            if (index != it.size - 1) {
                                if (index == 0) path.moveTo(offset.x, offset.y)
                                else path.lineTo(offset.x, offset.y)
                            }
                            // drawText for X-axis
                            drawContext.canvas.nativeCanvas.drawText(
                                values[index].x
                                    .roundToInt()
                                    .toString(),
                                offset.x,
                                canvasHeight + 25f,
                                paint
                            )

                            // drawText for Y-axis
                            drawContext.canvas.nativeCanvas.drawText(
                                values[index].y
                                    .roundToInt()
                                    .toString(),
                                -25f,
                                offset.y + 5,
                                paint
                            )
                        }

                        // drawPath for chart
                        drawPath(
                            path = path,
                            color = Color.Blue,
                            style = Stroke(width = lineWidth)
                        )

                        // drawPoints for chart
                        drawPoints(
                            points = it,
                            pointMode = PointMode.Points,
                            cap = StrokeCap.Round,
                            strokeWidth = dotSize,
                            color = Color.LightGray,
                        )

                        // drawPoints for X-axis
                        drawPoints(
                            points = it
                                .subList(1, it.size)
                                .map { Offset(it.x, canvasHeight) },
                            pointMode = PointMode.Points,
                            cap = StrokeCap.Round,
                            strokeWidth = dotSize,
                            color = Color.LightGray,
                        )

                        // drawPoints for Y-axis
                        drawPoints(
                            points = it
                                .subList(1, it.size)
                                .map { Offset(0f, it.y) },
                            pointMode = PointMode.Points,
                            cap = StrokeCap.Round,
                            strokeWidth = dotSize,
                            color = Color.LightGray,
                        )
                    }
                })

            Text(
                modifier = modifier.padding(top = 16.dp),
                text = "(osa Y = strany, osa X = čtení)",
                style = MaterialTheme.typography.caption.copy(fontSize = 8.sp),
                color = Color.LightGray
            )
        }
    }
}

//@Composable
//fun ResizeWidthColumn(modifier: Modifier, resize: Boolean, mainContent: @Composable () -> Unit) {
//    SubcomposeLayout(modifier) { constraints ->
//        val mainPlaceables = subcompose("Main", mainContent).map {
//            // Here we measure the width/height of the child Composables
//            it.measure(Constraints())
//        }
//
//        //Here we find the max width/height of the child Composables
//        val maxSize = mainPlaceables.fold(IntSize.Zero) { currentMax, placeable ->
//            IntSize(
//                width = maxOf(currentMax.width, placeable.width),
//                height = maxOf(currentMax.height, placeable.height)
//            )
//        }
//
//        val resizedPlaceables: List<Placeable> =
//            subcompose("Dependent", mainContent).map {
//                if (resize) {
//                    /** Here we rewrite the child Composables to have the width ofwidest Composable*/
//                    it.measure(
//                        Constraints(minWidth = maxSize.width)
//                    )
//                } else {
//                    // Ask the child for its preferred size.
//                    it.measure(Constraints())
//                }
//            }
//
//        /** We can place the Composables on the screenwith layout() and the place() functions*/
//
//        layout(constraints.maxWidth, constraints.maxHeight) {
//            resizedPlaceables.forEachIndexed { index, placeable ->
//                val widthStart = resizedPlaceables.take(index).sumOf { it.measuredHeight }
//                placeable.place(0, widthStart)
//            }
//        }
//    }
//}


fun Modifier.vertical() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.height, placeable.width) {
        placeable.place(
            x = -(placeable.width / 2 - placeable.height / 2),
            y = -(placeable.height / 2 - placeable.width / 2)
        )
    }
}

fun Float.mapToRange(inMin: Float, inMax: Float, outMin: Float, outMax: Float) =
    (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin