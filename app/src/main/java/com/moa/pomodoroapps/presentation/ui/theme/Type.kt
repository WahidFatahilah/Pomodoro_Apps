package com.moa.pomodoroapps.presentation.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp
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
    */
)

val Primary = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 40.sp
    )
val Heading_H1 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    lineHeight = 27.sp
)

val Heading_H2 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.W600,
    fontSize = 16.sp,
    lineHeight = 24.sp
)
val Subtitle_2 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp,
    lineHeight = 21.sp
)



val Ket_2 = TextStyle(
    fontFamily = poppins,
    fontWeight = FontWeight.W400,
    fontSize = 12.sp,
    lineHeight = 18.sp
)