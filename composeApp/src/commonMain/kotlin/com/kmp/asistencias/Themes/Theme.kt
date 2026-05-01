package com.kmp.asistencias.Themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BlueDeep,
    onPrimary = Color.White,
    secondary = BlueLight,
    onSecondary = Color.White,
    tertiary = BlueCyan,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = GrayDark,
    onSurfaceVariant = TextSecondaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = BlueDeep,
    onPrimary = Color.White,
    secondary = BlueLight,
    onSecondary = Color.White,
    tertiary = BlueCyan,
    background = BackgroundWhite,
    surface = White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    surfaceVariant = GrayLight,
    onSurfaceVariant = Color.Gray
)

@Composable
fun AsistenciasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
