package com.kmp.asistencias.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.asistencias.Components.AttendanceMap
import com.kmp.asistencias.Components.SlideToActButton
import com.kmp.asistencias.components.HomeActivityCard

@Composable
fun Home() {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color(0xFFF8F9FA) // Color de fondo grisáceo suave
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar con indicador online
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0E0E0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(32.dp))
                        }
                        // Indicador verde
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                                .padding(2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Bienvenido,", color = Color.Gray, fontSize = 16.sp)
                        Text("Carlos Ruiz", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
                
                // Botón de notificación
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .shadow(1.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Fecha y Hora
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "LUNES, 24 DE OCTUBRE",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "08:45",
                    fontSize = 72.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1C1E)
                )
            }

            // Status Badge
            Surface(
                color = Color(0xFFFFF1F1),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF3B30))
                    )
                    Text(
                        text = "Fuera de turno",
                        color = Color(0xFFFF3B30),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Mapa Implementado
            AttendanceMap(
                onRecenterClick = { lat, lon ->
                    // Aquí podrías disparar la lógica de GPS real con lat/lon
                    println("Recentering to: $lat, $lon")
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Actividad Reciente Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "ACTIVIDAD RECIENTE",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                TextButton(onClick = { /* Navegar al historial */ }) {
                    Text("Ver historial", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            // Activity Card
            HomeActivityCard(
                title = "Salida registrada",
                subtitle = "Ayer",
                time = "18:02",
                icon = Icons.AutoMirrored.Filled.Logout
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Botón Deslizar
            SlideToActButton(
                text = "Desliza para entrar",
                onConfirm = { /* Acción de entrar */ }
            )
            
            Text(
                "Asegúrate de estar en tu zona de trabajo",
                modifier = Modifier.padding(top = 12.dp),
                color = Color.LightGray,
                fontSize = 12.sp
            )
        }
    }
}
