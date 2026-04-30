package com.kmp.asistencias.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import com.kmp.asistencias.Components.HistoryItem
import com.kmp.asistencias.Components.LoadingOverlay
import com.kmp.asistencias.Models.PerfilUsuarioResponse
import com.kmp.asistencias.Models.RequestFoto
import com.kmp.asistencias.Models.Documento
import com.kmp.asistencias.Services.Perfil as PerfilService
import com.russhwolf.settings.Settings
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import io.ktor.util.encodeBase64
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun Perfil(onLogout: () -> Unit) {
    val settings = remember { Settings() }
    val scope = rememberCoroutineScope()
    var perfilData by remember { mutableStateOf<PerfilUsuarioResponse?>(null) }
    var fotoUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isContentVisible by remember { mutableStateOf(false) }

    @OptIn(ExperimentalEncodingApi::class)
    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let { byteArray ->
                scope.launch {
                    try {
                        isLoading = true
                        val base64Image = Base64.Default.encode(byteArray)
                        val request = RequestFoto(
                            idUsuario = 1, // TODO: Obtener el ID real
                            documento = Documento(
                                idTipoDocumento = 10,
                                base64 = base64Image,
                                extension = "jpg"
                            )
                        )
                        val response = PerfilService.CambiarFoto(request)
                        if (response.success) {
                            val fotoResponse = PerfilService.ObtenerFoto()
                            fotoUrl = fotoResponse.data
                        }
                    } catch (e: Exception) {
                        println("Error al cambiar foto: ${e.message}")
                    } finally {
                        isLoading = false
                    }
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        try {
            perfilData = PerfilService.getPerfil()
            val fotoResponse = PerfilService.ObtenerFoto()
            fotoUrl = fotoResponse.data // Asumimos que la URL viene en 'message'
            println("Error fetching perfil: $fotoUrl")

        } catch (e: Exception) {
            println("Error fetching perfil: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    val profile = perfilData?.data?.perfil?.firstOrNull()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isContentVisible) {
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
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable { singleImagePicker.launch() },
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

                        // Icono de edición (Cámara)
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF007AFF))
                                .clickable { singleImagePicker.launch() }
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Cambiar foto",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = profile?.nombreCompleto ?: "Invitado",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = profile?.cargo ?: "",
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val registros = perfilData?.data?.ultimosRegistros ?: emptyList()
                    if (registros.isEmpty()) {
                        Text(
                            text = "No hay registros recientes",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    } else {
                        registros.forEachIndexed { index, registro ->
                            val isEntry = registro.tipo.contains("Entrada", ignoreCase = true)
                            val displayTime = try {
                                registro.fechaHora.split("T").last().substring(0, 5)
                            } catch (e: Exception) {
                                registro.fechaHora
                            }

                            val displayDate = try {
                                registro.fechaHora.split("T").first()
                            } catch (e: Exception) {
                                ""
                            }

                            HistoryItem(
                                isEntry = isEntry,
                                title = "${registro.tipo} - $displayDate",
                                time = displayTime,
                                location = "Ubicación registrada",
                                isLast = index == registros.size - 1
                            )
                        }
                    }
                }

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
        }

        LoadingOverlay(
            isLoading = isLoading,
            onFinished = { isContentVisible = true }
        )
    }
}
