package com.kmp.asistencias.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.kmp.asistencias.Components.AttendanceMap
import com.kmp.asistencias.Components.HomeActivityCard
import com.kmp.asistencias.Components.SlideToActButton
import com.kmp.asistencias.Components.SuccessOverlay
import com.kmp.asistencias.Themes.BackgroundWhite
import com.kmp.asistencias.Themes.GrayBlue
import com.kmp.asistencias.Utils.obtenerFechaActual
import com.kmp.asistencias.Utils.obtenerHoraActual
import com.kmp.asistencias.Models.RequestEntradaSalida
import com.kmp.asistencias.Network.Home as HomeApi
import com.kmp.asistencias.Services.Perfil as PerfilService
import com.russhwolf.settings.Settings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Home(onNavigateToHistory: () -> Unit) {
    val scope = rememberCoroutineScope()
    val settings = remember { Settings() }
    var estaEnTurno by remember { mutableStateOf(settings.getBoolean("statusTurno", false)) }


    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("...") }
    var fotoUrl by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val perfilResponse = PerfilService.getPerfil()
                userName = perfilResponse.data.perfil.firstOrNull()?.nombreCompleto ?: "Usuario"
                
                val fotoResponse = PerfilService.ObtenerFoto()
                fotoUrl = fotoResponse.data
            } catch (e: Exception) {
                println("Error fetching profile in Home: ${e.message}")
            }
        }
        while (true) {
            fecha = obtenerFechaActual()
            hora = obtenerHoraActual()
            delay(1000)
        }
    }

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
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
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.Gray
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50))
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text("Bienvenido,", color = Color.Gray, fontSize = 16.sp)
                        Text(userName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(GrayBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notificaciones"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Fecha y Hora
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = fecha,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )

                Text(
                    text = hora,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1C1E)
                )
            }

            // Status Badge
            Surface(
                color = if (estaEnTurno) Color(0xFFE8F5E9) else Color(0xFFFFF1F1),
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
                            .background(if (estaEnTurno) Color(0xFF4CAF50) else Color(0xFFFF3B30))
                    )

                    Text(
                        text = if (estaEnTurno) "En turno"  else "Fuera de turno",
                        color = if (estaEnTurno) Color(0xFF4CAF50) else Color(0xFFFF3B30),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            AttendanceMap(
                onRecenterClick = { lat, lon ->
                    println("Recentering to: $lat, $lon")
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            SlideToActButton(
                text = if (estaEnTurno) "Desliza para Salida" else "Desliza para Registrarte",
                onConfirm = {
                    scope.launch {
                        try {
                            val lat = settings.getDouble("last_lat", 0.0)
                            val lon = settings.getDouble("last_lon", 0.0)
                            
                            val request = RequestEntradaSalida(
                                IdUsuario = 1, // TODO: Obtener el ID real del usuario logueado
                                Latitud = lat,
                                Longitud = lon,
                                UbicacionDetalle = "Ubicación desde App Movil",
                                Fuente = "APP_MOVIL"
                            )

                            val response = HomeApi.RegistarEntrada(request,estaEnTurno)
                            
                            if (response.status == "Success") {
                                estaEnTurno = !estaEnTurno
                                showSuccess = true
                                println("Registro exitoso: ${response.message}")
                                settings.putBoolean("statusTurno", !settings.getBoolean("statusTurno", false))                            } else {

                                println("Error en registro: ${response.message}")
                            }
                        } catch (e: Exception) {
                            println("Error de red: ${e.message}")
                        }
                    }
                }
            )

            Text(
                text = "Asegúrate de estar en tu zona de trabajo",
                modifier = Modifier.padding(top = 12.dp),
                color = Color.LightGray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Actividad Reciente Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIVIDAD RECIENTE",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                TextButton(
                    onClick = onNavigateToHistory
                ) {
                    Text(
                        text = "Ver historial",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HomeActivityCard(
                title = "Salida registrada",
                subtitle = "Ayer",
                time = "18:02",
                icon = Icons.AutoMirrored.Filled.Logout
            )


            Spacer(modifier = Modifier.height(120.dp))
        }

        SuccessOverlay(
            isVisible = showSuccess,
            onFinished = { showSuccess = false }
        )
    }
}
