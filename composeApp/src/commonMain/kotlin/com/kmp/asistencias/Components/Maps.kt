package com.kmp.asistencias.Components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.Locator
import dev.jordond.compass.geolocation.mobile.mobile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Componente de Mapa interactivo con ubicación real.
 */
@Composable
fun AttendanceMap(
    modifier: Modifier = Modifier,
    locationName: String = "Buscando ubicación...",
    onRecenterClick: (Double, Double) -> Unit = { _, _ -> }
) {
    val scope = rememberCoroutineScope()
    var isRecenterAnimating by remember { mutableStateOf(false) }
    var currentLocationName by remember(locationName) { mutableStateOf(locationName) }
    
    // Inicializamos el Geolocator manualmente si el helper de compose falla
    val geolocator = remember { Geolocator(Locator.mobile()) }
    
    // Animación de rebote para el Pin
    val infiniteTransition = rememberInfiniteTransition()
    val pinBounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isRecenterAnimating) -15f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Estado para las coordenadas actuales del mapa
    var mapLat by remember { mutableStateOf(19.4326) }
    var mapLon by remember { mutableStateOf(-99.1332) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color(0xFFE8EDF2))
    ) {
        // Mapa Real
        GoogleMapView(
            modifier = Modifier.fillMaxSize(),
            latitude = mapLat,
            longitude = mapLon,
            zoom = 15f,
            onCameraChange = { lat, lon ->
                mapLat = lat
                mapLon = lon
                currentLocationName = "${lat.toString().take(8)}, ${lon.toString().take(8)}"
            }
        )

        // Overlay de información
        Surface(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.TopStart),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color(0xFF007AFF)
                )
                Column {
                    Text(
                        "UBICACIÓN ACTUAL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.LightGray
                    )
                    Text(
                        currentLocationName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Botón interactivo de Recentrar
        Box(
            modifier = Modifier
                .padding(end = 20.dp)
                .align(Alignment.BottomStart)
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable {
                    scope.launch {
                        isRecenterAnimating = true
                        try {
                            // Intentamos obtener la ubicación actual
                            geolocator.current(Priority.HighAccuracy)
                                .onSuccess { location ->

                                    val lat = location.coordinates.latitude
                                    val lon = location.coordinates.longitude

                                    // Actualiza el marcador
                                    mapLat = lat
                                    mapLon = lon

                                    // Actualiza el texto de ubicación
                                    currentLocationName = "${lat.toString().take(8)}, ${lon.toString().take(8)}"

                                    // Fuerza que la vista del mapa se recenter siempre
                                    onRecenterClick(lat, lon)
                                }
                                .onFailed { error ->
                                    currentLocationName = "Error: ${error.message}"
                                }
                        } catch (e: Exception) {
                            currentLocationName = "Permiso denegado o error"
                        }
                        delay(1200)
                        isRecenterAnimating = false
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Recenter",
                tint = if (isRecenterAnimating) Color(0xFF007AFF) else Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}



@Composable
expect fun GoogleMapView(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    zoom: Float = 15f,
    onCameraChange: (Double, Double) -> Unit = { _, _ -> }
)
