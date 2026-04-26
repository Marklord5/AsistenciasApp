package com.kmp.asistencias.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kmp.asistencias.components.AttendanceHistoryCard
import com.kmp.asistencias.components.HistorySectionHeader

@Composable
fun Historial() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
            // SECCIÓN HOY
            HistorySectionHeader(title = "Hoy", subtitle = "12 Oct")
            AttendanceHistoryCard(
                startTime = "09:00 AM",
                endTime = "--:--",
                location = "Oficina Madrid Central",
                locationIcon = Icons.Default.LocationOn,
                status = "En curso",
                statusColor = Color(0xFFF6E711),
                mainIcon = Icons.Default.Timer,
                mainIconColor = Color.Black,
                mainIconBgColor = Color(0xFFFFF9C4),
                showLeftStripe = true,
                stripeColor = Color(0xFFF6E711)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // SECCIÓN AYER
            HistorySectionHeader(title = "Ayer", subtitle = "11 Oct")
            AttendanceHistoryCard(
                startTime = "08:55 AM",
                endTime = "06:10 PM",
                location = "Trabajo Remoto",
                locationIcon = Icons.Default.Home,
                status = "Completado",
                statusColor = Color(0xFFF1F1F1),
                statusTextColor = Color(0xFF8E8E93),
                mainIcon = Icons.Default.CheckCircle,
                mainIconColor = Color(0xFF4CAF50),
                mainIconBgColor = Color.White,
                extraBadgeText = "9h 15m"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // SECCIÓN 10 DE OCTUBRE
            HistorySectionHeader(title = "10 de Octubre", subtitle = "Jueves")
            AttendanceHistoryCard(
                startTime = "09:10 AM",
                endTime = "05:00 PM",
                location = "Cliente - Sede Norte",
                locationIcon = Icons.Default.Business,
                status = "Completado",
                statusColor = Color(0xFFF1F1F1),
                statusTextColor = Color(0xFF8E8E93),
                mainIcon = Icons.Default.CheckCircle,
                mainIconColor = Color(0xFF4CAF50),
                mainIconBgColor = Color.White,
                extraBadgeText = "7h 50m"
            )

            Spacer(modifier = Modifier.height(12.dp))

            AttendanceHistoryCard(
                startTime = "10:30 AM",
                endTime = "07:00 PM",
                location = "Oficina Madrid Central",
                locationIcon = Icons.Default.LocationOn,
                status = "Completado",
                statusColor = Color(0xFFF1F1F1),
                statusTextColor = Color(0xFF8E8E93),
                mainIcon = Icons.Default.Warning,
                mainIconColor = Color.Red,
                mainIconBgColor = Color(0xFFFFEBEE),
                extraBadgeText = "Llegada tarde",
                extraBadgeColor = Color(0xFFFFEBEE),
                extraBadgeTextColor = Color.Red
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
}
