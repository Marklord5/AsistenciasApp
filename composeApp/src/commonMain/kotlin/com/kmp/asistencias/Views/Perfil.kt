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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kmp.asistencias.Components.LogoutCard
import com.kmp.asistencias.Components.StatCard
import com.kmp.asistencias.Components.RecentHistorySection
import com.kmp.asistencias.Components.LoadingOverlay
import com.kmp.asistencias.Models.PerfilUsuarioResponse
import com.kmp.asistencias.Services.Perfil as PerfilService
import com.russhwolf.settings.Settings

@Composable
fun Perfil(onLogout: () -> Unit) {
    val settings = remember { Settings() }
    var perfilData by remember { mutableStateOf<PerfilUsuarioResponse?>(null) }
    var fotoUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            perfilData = PerfilService.getPerfil()
            val fotoResponse = PerfilService.ObtenerFoto()
            fotoUrl = fotoResponse.data // Asumimos que la URL viene en 'message'
            println("Error fetching perfil: ${fotoUrl}")

        } catch (e: Exception) {
            println("Error fetching perfil: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    val profile = perfilData?.data?.perfil?.firstOrNull()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F2F7)) // Fondo gris claro estilo iOS
                .verticalScroll(rememberScrollState())
        ) {
            // ... resto del contenido ...
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
                if (fotoUrl != null) {
                    AsyncImage(
                        model = fotoUrl,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = Color(0xFF8E8E93)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = profile?.nombreCompleto ?: if (isLoading) "Cargando..." else "Invitado",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = profile?.cargo ?: if (isLoading) "Cargando..." else "",
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
                value = perfilData?.data?.porcentajePuntualidad?.toString() ?: "0",
                unit = "%",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.AssignmentInd,
                label = "Horas Hoy",
                value = perfilData?.data?.horasHoy?.take(4) ?: "0",
                unit = "hrs",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección de Historial Reciente
        RecentHistorySection(
            registros = perfilData?.data?.ultimosRegistros ?: emptyList(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Cerrar Sesión
        LogoutCard(
            onClick = {
                onLogout()
                settings.remove("token")
                settings.remove("tokenRefresh")
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        )
        Spacer(modifier = Modifier.height(120.dp))
    }

    LoadingOverlay(isLoading = isLoading)
    }
}
