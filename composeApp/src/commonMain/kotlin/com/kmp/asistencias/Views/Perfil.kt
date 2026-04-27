package com.kmp.asistencias.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.asistencias.components.LogoutCard
import com.kmp.asistencias.components.StatCard
import com.kmp.asistencias.components.RecentHistorySection

@Composable
fun Perfil(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7)) // Fondo gris claro estilo iOS
            .verticalScroll(rememberScrollState())
    ) {
        // Encabezado del Perfil
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color(0xFF8E8E93)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Anthony Cast",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Desarrollador Mobile",
                fontSize = 16.sp,
                color = Color(0xFF8E8E93)
            )
        }

        // Fila de Tarjetas de Estadísticas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                icon = Icons.Default.Star,
                label = "Puntualidad",
                value = "98",
                unit = "%",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.AssignmentInd,
                label = "Asistencias",
                value = "22",
                unit = "días",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección de Historial Reciente
        RecentHistorySection(
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Cerrar Sesión
        LogoutCard(
            onClick = {
                onLogout()
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        )
    }
}
