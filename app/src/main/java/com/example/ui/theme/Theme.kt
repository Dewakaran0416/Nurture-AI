package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = MinimalDarkPrimary,
    onPrimary = MinimalDarkOnPrimary,
    secondary = MinimalDarkSecondary,
    tertiary = MinimalDarkTertiary,
    background = MinimalDarkBackground,
    surface = MinimalDarkSurface,
    onBackground = MinimalDarkOnBackground,
    onSurface = MinimalDarkOnSurface,
    outline = MinimalDarkSecondary.copy(alpha = 0.5f),
    outlineVariant = MinimalDarkSecondary.copy(alpha = 0.2f)
)

private val LightColorScheme = lightColorScheme(
    primary = MinimalPrimary,
    onPrimary = MinimalOnPrimary,
    secondary = MinimalSecondary,
    tertiary = MinimalTertiary,
    background = MinimalBackground,
    surface = MinimalSurface,
    onBackground = MinimalOnBackground,
    onSurface = MinimalOnSurface,
    outline = MinimalSecondary.copy(alpha = 0.5f),
    outlineVariant = MinimalSecondary.copy(alpha = 0.15f)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
