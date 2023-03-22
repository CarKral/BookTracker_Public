package com.example.book_tracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//val Primary = Color(0xFF1A237E)
//val PrimaryVariant = Color(0xFF311B92)
//val Secondary = Color(0xFF01579B)
//val SecondaryVariant = Color(0xFF0D47A1)
//
//private val lightColorsTheme = lightColors(
//    primary = Color(0xFF0034EE),
//    primaryVariant = Color(0xFF0012B3),
//    secondary = Color(0xFFFFFF00),
//    secondaryVariant = Color(0xFFA6FF00),
//    background = Color.White,
////    surface = Color.White,
//    surface = Color(0xFFE3F2FD),
//    error = Color(0xFFB00020),
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
//    onError = Color.White
//)

private val darkColorsTheme = darkColors(
    primary = Color(0xFF5282FF),
    primaryVariant = Color(0xFF0012B3),
    secondary = Color(0xFF0320DA),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Color(0xFFCF6679),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black
)

@Composable
fun BookTrackerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colors =
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//            darkColorsTheme
//        } else {
//            when {
//                darkTheme -> darkColorsTheme
//                else -> lightColorsTheme
//            }
//        }

    MaterialTheme(
        colors = darkColorsTheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
