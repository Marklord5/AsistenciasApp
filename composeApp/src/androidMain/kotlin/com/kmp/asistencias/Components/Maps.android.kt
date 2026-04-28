package com.kmp.asistencias.Components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@Composable
actual fun GoogleMapView(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    zoom: Float,
    onCameraChange: (Double, Double) -> Unit
) {
    val location = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, zoom)
    }

    // Propiedades para activar la "bolita azul" de ubicación real
    val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = false)) }
    val properties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }

    // Sincronizamos la cámara si la posición cambia externamente (ej: botón de recentrar)
    LaunchedEffect(latitude, longitude) {
        val currentTarget = cameraPositionState.position.target
        if (Math.abs(currentTarget.latitude - latitude) > 0.0001 ||
            Math.abs(currentTarget.longitude - longitude) > 0.0001) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(location, zoom)
            )
        }
    }

    // Notificamos cambios de cámara al mover el mapa manualmente
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            val target = cameraPositionState.position.target
            if (target.latitude != 0.0 && target.longitude != 0.0) {
                onCameraChange(target.latitude, target.longitude)
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        onMapClick = {},
        onMapLongClick = {}
    )
}
