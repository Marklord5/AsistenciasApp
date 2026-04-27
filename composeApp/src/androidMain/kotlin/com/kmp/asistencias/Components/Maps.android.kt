package com.kmp.asistencias.Components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun GoogleMapView(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    zoom: Float
) {
    val location = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, zoom)
    }
    val markerState = rememberMarkerState(position = location)

    // Actualizamos la cámara y el marcador cuando cambian las coordenadas
    LaunchedEffect(latitude, longitude) {
        markerState.position = location
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(location, zoom)
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = markerState,
            title = "Tu ubicación"
        )
    }
}
