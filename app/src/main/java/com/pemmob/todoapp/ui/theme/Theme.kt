package com.pemmob.todoapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GreenLightActive,
    onPrimary = GreenDarker,
    secondary = GreenLight,
    onSecondary = GreenDarker,
    background = GreenDarker,
    onBackground = GreenLight,
    surface = GreenDarkActive,
    onSurface = GreenLight,
    error = Error
)

private val LightColorScheme = lightColorScheme(
    primary = GreenNormal,
    onPrimary = White,
    secondary = GreenLightActive,
    onSecondary = GreenDarker,
    background = AppBackground,
    onBackground = TextPrimary,
    surface = White,
    onSurface = TextPrimary,
    error = Error
)

@Composable
fun ToDoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
