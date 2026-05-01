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
    background = BackgroundDark, // 0xFF000000
    surface = SurfaceDark,       // 0xFF1C1C1E
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = GrayDark,   // 0xFF2C2C2E
    onSurfaceVariant = TextSecondaryDark, // 0xFF8E8E93
    outlineVariant = Color(0xFF3A3A3C)
)

private val LightColorScheme = lightColorScheme(
    primary = BlueDeep,
    onPrimary = Color.White,
    secondary = BlueLight,
    onSecondary = Color.White,
    tertiary = BlueCyan,
    background = BackgroundWhite,
    surface = White,             // 0xFFFFFFFF
    onBackground = Color(0xFF1A1C1E),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFF1F1F1), 
    onSurfaceVariant = Color(0xFF8E8E93),
    outlineVariant = GrayLight
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
