package com.kmp.asistencias.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kmp.asistencias.Components.AttendanceHistoryCard
import com.kmp.asistencias.Components.HistorySectionHeader
import com.kmp.asistencias.Components.LoadingOverlay
import com.kmp.asistencias.Models.HistorialData

@Composable
fun Historial() {
    var historialData by remember { mutableStateOf<HistorialData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isContentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val response = com.kmp.asistencias.Network.Historial.Historial()
            historialData = response.data
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isContentVisible) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                historialData?.detallePorDia?.forEach { detalle ->
                    // Recortar fecha de forma simple (YYYY-MM-DD)
                    val datePart = detalle.fecha.split("T").getOrNull(0) ?: ""
                    val parts = datePart.split("-")
                    val day = parts.getOrNull(2) ?: ""
                    val monthIdx = parts.getOrNull(1)?.toIntOrNull() ?: 0
                    
                    val monthNames = listOf("", "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")
                    val monthFull = listOf("", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
                    
                    val monthName = monthNames.getOrNull(monthIdx) ?: ""
                    val monthFullName = monthFull.getOrNull(monthIdx) ?: ""
                    
                    val title = "$day de $monthFullName"
                    val subtitle = "$day $monthName"

                    HistorySectionHeader(title = title, subtitle = subtitle)
                    
                    val startTime = formatISOToTime(detalle.horaEntrada)
                    val endTime = formatISOToTime(detalle.horaSalida)
                    val isCompletado = detalle.horaSalida != null && detalle.horaSalida.isNotEmpty()
                    
                    AttendanceHistoryCard(
                        startTime = startTime,
                        endTime = endTime,
                        location = "Ubicación registrada",
                        locationIcon = Icons.Default.LocationOn,
                        status = if (isCompletado) "Completado" else "En curso",
                        statusColor = if (isCompletado) MaterialTheme.colorScheme.surfaceVariant else Color(0xFFF6E711),
                        statusTextColor = if (isCompletado) MaterialTheme.colorScheme.onSurfaceVariant else Color.Black,
                        mainIcon = if (isCompletado) Icons.Default.CheckCircle else Icons.Default.Timer,
                        mainIconColor = if (isCompletado) Color(0xFF4CAF50) else Color.Black,
                        mainIconBgColor = if (isCompletado) MaterialTheme.colorScheme.surface else Color(0xFFFFF9C4),
                        extraBadgeText = if (detalle.horasTrabajadas > 0) "${detalle.horasTrabajadas}h" else null,
                        showLeftStripe = !isCompletado,
                        stripeColor = Color(0xFFF6E711)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        LoadingOverlay(
            isLoading = isLoading,
            fileName = "Loader_Paperplane.json",
            onFinished = { isContentVisible = true }
        )
    }
}

fun formatISOToTime(isoString: String?): String {
    if (isoString == null || isoString.isEmpty() || !isoString.contains("T")) return "--:--"
    return try {
        val timePart = isoString.split("T")[1]
        val components = timePart.split(":")
        val hour = components[0].toInt()
        val minute = components[1]
        
        val ampm = if (hour >= 12) "PM" else "AM"
        val displayHour = if (hour % 12 == 0) 12 else hour % 12
        "$displayHour:$minute $ampm"
    } catch (e: Exception) {
        "--:--"
    }
}
