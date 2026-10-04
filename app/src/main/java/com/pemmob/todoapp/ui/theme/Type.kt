package com.pemmob.todoapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ManropeFontFamily = FontFamily.SansSerif

// H1 Bold 20px
val H1Bold = TextStyle(
    fontFamily = ManropeFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 28.sp
)

// H2 SemiBold 18px
val H2SemiBold = TextStyle(
    fontFamily = ManropeFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    lineHeight = 24.sp
)

// Body Large Medium 16px
val BodyLargeMedium = TextStyle(
    fontFamily = ManropeFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp
)

// Body Small Regular 14px
val BodySmallRegular = TextStyle(
    fontFamily = ManropeFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp
)

// Caption/Label SemiBold 12px
val CaptionSemiBold = TextStyle(
    fontFamily = ManropeFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    lineHeight = 16.sp
)

val Typography = Typography(
    headlineLarge = H1Bold,
    headlineMedium = H2SemiBold,
    titleLarge = H2SemiBold,
    bodyLarge = BodyLargeMedium,
    bodyMedium = BodySmallRegular,
    bodySmall = BodySmallRegular,
    labelLarge = BodyLargeMedium,
    labelMedium = CaptionSemiBold,
    labelSmall = CaptionSemiBold
)
