package com.kmp.asistencias.Components

// Importaciones necesarias para animaciones, diseño, íconos, estados y ubicación
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.Locator
import dev.jordond.compass.geolocation.mobile.mobile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@Composable
fun AttendanceMap(
    modifier: Modifier = Modifier,
    onRecenterClick: (Double, Double) -> Unit = { _, _ -> }
) {
    // Permite ejecutar tareas en segundo plano, como pedir la ubicación
    val scope = rememberCoroutineScope()

    // Guarda y lee la última ubicación conocida
    val settings = remember { Settings() }

    // Objeto encargado de obtener la ubicación del dispositivo
    val geolocator = remember { Geolocator(Locator.mobile()) }

    // Coordenadas usadas para centrar el mapa
    var mapLat by remember { mutableStateOf(settings.getDouble("last_lat", 0.0)) }
    var mapLon by remember { mutableStateOf(settings.getDouble("last_lon", 0.0)) }

    // Coordenadas reales del usuario
    var userLat by remember { mutableStateOf(settings.getDouble("last_lat", 0.0)) }
    var userLon by remember { mutableStateOf(settings.getDouble("last_lon", 0.0)) }

    // Indica si se está buscando la ubicación
    var isFetching by remember { mutableStateOf(false) }

    // Actualiza la ubicación, centra el mapa y guarda los datos
    fun updateLocation(newLat: Double, newLon: Double) {
        userLat = newLat
        userLon = newLon
        mapLat = newLat
        mapLon = newLon
        settings["last_lat"] = newLat
        settings["last_lon"] = newLon
    }

    // Se ejecuta al cargar la pantalla para obtener la ubicación inicial
    LaunchedEffect(Unit) {
        // Obtiene una ubicación rápida guardada por el sistema
        geolocator.lastLocation().onSuccess {
            updateLocation(it.coordinates.latitude, it.coordinates.longitude)
        }

        // Obtiene una ubicación más actual
        geolocator.current(Priority.Balanced).onSuccess {
            updateLocation(it.coordinates.latitude, it.coordinates.longitude)
        }
    }

    // Contenedor principal del mapa
    Box(
        modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Muestra el mapa usando las coordenadas actuales
        GoogleMapView(
            modifier = Modifier.fillMaxSize(),
            latitude = mapLat,
            longitude = mapLon,

            // Si el usuario mueve el mapa, se actualiza la posición visual
            onCameraChange = { nLat, nLon ->
                mapLat = nLat
                mapLon = nLon
            }
        )

        // Tarjeta que muestra las coordenadas del usuario
        Surface(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.TopStart),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.MyLocation, null, Modifier.size(18.dp), Color(0xFF007AFF))

                Column {
                    Text(
                        "COORDENADAS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        "${userLat.toString().take(8)}, ${userLon.toString().take(8)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Botón para volver a centrar el mapa en la ubicación actual
        Box(
            Modifier
                .padding(20.dp)
                .align(Alignment.BottomStart)
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    scope.launch {
                        isFetching = true

                        // Busca la ubicación exacta actual
                        geolocator.current(Priority.HighAccuracy).onSuccess {
                            updateLocation(
                                it.coordinates.latitude,
                                it.coordinates.longitude
                            )

                            // Notifica hacia afuera la nueva ubicación
                            onRecenterClick(userLat, userLon)
                        }

                        delay(800)
                        isFetching = false
                    }
                },
            Alignment.Center
        ) {
            Icon(
                Icons.Default.MyLocation,
                null,
                Modifier.size(24.dp),
                if (isFetching) Color(0xFF007AFF) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Función esperada para mostrar el mapa según la plataforma
@Composable
expect fun GoogleMapView(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    zoom: Float = 15f,
    onCameraChange: (Double, Double) -> Unit = { _, _ -> }
)