package com.kmp.asistencias.Themes

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Colores base extraídos de la imagen
val BlueLight = Color(0xFF4DA9FF) // Azul claro vibrante
val BlueDeep = Color(0xFF5A57FF)  // Azul profundo / Violeta
val BlueCyan = Color(0xFF35C2FB)  // Cyan para los reflejos más claros

// Colores de fondo y acento
val BackgroundWhite = Color(0xFFF8FAFF)
val White = Color(0xFFFFFFFF)
val GrayLight = Color(0xFFE1E8F0)

val GrayBlue = Color(0xFFF2F6FF)

// Degradado principal basado en el logo
val PrimaryGradient = Brush.linearGradient(
    colors = listOf(BlueCyan, BlueDeep)
)

// Degradado alternativo para botones o estados
val SecondaryGradient = Brush.verticalGradient(
    colors = listOf(BlueLight, BlueDeep)
)

