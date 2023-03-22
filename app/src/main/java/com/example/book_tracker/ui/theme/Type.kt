package com.example.book_tracker.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with

val typography = Typography(
    h1 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp,
        color = MyTextColor
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp,
        color = MyTextColor
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        letterSpacing = 0.sp,
        color = MyTextColor
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = 0.25.sp,
        color = MyTextColor
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.sp,
        color = MyTextColor
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp,
        color = MyTextColor
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
        color = MyTextColor
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
        color = MyTextColor
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp,
        color = MyTextColor
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
        color = MyTextColor
    ),
    button = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 1.25.sp,
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp,
        color = MyTextColor
    ),
    overline = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 1.5.sp,
        color = MyTextColor
    )
)

/* Other default text styles to override
button = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp
),
caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
//    */
//    h1: TextStyle = TextStyle(
//    fontWeight = FontWeight.Light,
//    fontSize = 96.sp,
//    letterSpacing = (-1.5).sp
//    ),
//    h2: TextStyle = TextStyle(
//    fontWeight = FontWeight.Light,
//    fontSize = 60.sp,
//    letterSpacing = (-0.5).sp
//    ),
//    h3: TextStyle = TextStyle(
//    fontWeight = FontWeight.Normal,
//    fontSize = 48.sp,
//    letterSpacing = 0.sp
//    ),
//    h4: TextStyle = TextStyle(
//    fontWeight = FontWeight.Normal,
//    fontSize = 34.sp,
//    letterSpacing = 0.25.sp
//    ),
//    h5: TextStyle = TextStyle(
//    fontWeight = FontWeight.Normal,
//    fontSize = 24.sp,
//    letterSpacing = 0.sp
//    ),
//    h6: TextStyle = TextStyle(
//    fontWeight = FontWeight.Medium,
//    fontSize = 20.sp,
//    letterSpacing = 0.15.sp
//    ),
//    subtitle1: TextStyle = TextStyle(
//    fontWeight = FontWeight.Normal,
//    fontSize = 16.sp,
//    letterSpacing = 0.15.sp
//    ),
//    subtitle2: TextStyle = TextStyle(
//    fontWeight = FontWeight.Medium,
//    fontSize = 14.sp,
//    letterSpacing = 0.1.sp
//    ),
//    body1: TextStyle = TextStyle(
//    fontWeight = FontWeight.Normal,
//    fontSize = 16.sp,
//    letterSpacing = 0.5.sp
//    ),
//    body2: TextStyle = TextStyle(
//    fontWeight = FontWeight.Normal,
//    fontSize = 14.sp,
//    letterSpacing = 0.25.sp
//    ),
//    button: TextStyle = TextStyle(
//    fontWeight = FontWeight.Medium,
//    fontSize = 14.sp,
//    letterSpacing = 1.25.sp
//    ),
//    caption: TextStyle = TextStyle(
//    fontWeight = FontWeight.Normal,
//    fontSize = 12.sp,
//    letterSpacing = 0.4.sp
//    ),
//    overline: TextStyle = TextStyle(
//    fontWeight = FontWeight.Normal,
//    fontSize = 10.sp,
//    letterSpacing = 1.5.sp
//    )
