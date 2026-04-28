package com.kmp.asistencias.Components

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
    val scope = rememberCoroutineScope()
    val settings = remember { Settings() }
    val geolocator = remember { Geolocator(Locator.mobile()) }

    // Coordenadas que controlan la cámara del mapa
    var mapLat by remember { mutableStateOf(settings.getDouble("last_lat", 0.0)) }
    var mapLon by remember { mutableStateOf(settings.getDouble("last_lon", 0.0)) }

    // Coordenadas reales del usuario (solo se actualizan por GPS)
    var userLat by remember { mutableStateOf(settings.getDouble("last_lat", 0.0)) }
    var userLon by remember { mutableStateOf(settings.getDouble("last_lon", 0.0)) }

    var isFetching by remember { mutableStateOf(false) }

    // Actualiza la ubicación del usuario y recentra el mapa
    fun updateLocation(newLat: Double, newLon: Double) {
        userLat = newLat
        userLon = newLon
        mapLat = newLat
        mapLon = newLon
        settings["last_lat"] = newLat
        settings["last_lon"] = newLon
    }

    LaunchedEffect(Unit) {
        // 1. Intentar ubicación de caché del sistema (Casi instantáneo)
        geolocator.lastLocation().onSuccess { updateLocation(it.coordinates.latitude, it.coordinates.longitude) }
        
        // 2. Refinar con ubicación actual en segundo plano
        geolocator.current(Priority.Balanced).onSuccess { updateLocation(it.coordinates.latitude, it.coordinates.longitude) }
    }

    Box(modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(40.dp)).background(Color(0xFFE8EDF2))) {
        GoogleMapView(
            modifier = Modifier.fillMaxSize(),
            latitude = mapLat,
            longitude = mapLon,
            onCameraChange = { nLat, nLon -> mapLat = nLat; mapLon = nLon }
        )



        Surface(
            Modifier.padding(20.dp).align(Alignment.TopStart),
            RoundedCornerShape(24.dp), Color.White.copy(0.95f), shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.MyLocation, null, Modifier.size(18.dp), Color(0xFF007AFF))
                Column {
                    Text("COORDENADAS", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color.Gray)
                    Text("${userLat.toString().take(8)}, ${userLon.toString().take(8)}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Botón Recentrar simple
        Box(
            Modifier.padding(20.dp).align(Alignment.BottomStart).size(56.dp).clip(CircleShape).background(Color.White)
                .clickable {
                    scope.launch {
                        isFetching = true
                        geolocator.current(Priority.HighAccuracy).onSuccess {
                            updateLocation(it.coordinates.latitude, it.coordinates.longitude)
                            onRecenterClick(userLat, userLon)
                        }
                        delay(800)
                        isFetching = false
                    }
                },
            Alignment.Center
        ) {
            Icon(Icons.Default.MyLocation, null, Modifier.size(24.dp), if (isFetching) Color(0xFF007AFF) else Color.Black)
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
