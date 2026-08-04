package com.kmp.asistencias.Components

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.kmp.asistencias.Utils.LocationProvider

@Composable
actual fun GoogleMapView(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    zoom: Float,
    onCameraChange: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    val location = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, zoom)
    }

    // En Android el permiso es en tiempo de ejecución: activar isMyLocationEnabled
    // sin tenerlo concedido lanza SecurityException y tira la app en el primer arranque.
    // Consultamos el estado real al sistema y lo re-evaluamos cada vez que
    // LocationProvider confirma o niega el permiso (primera petición, volver de Ajustes, refresh).
    val estadoPermiso by LocationProvider.permisoUbicacion.collectAsState()
    val permisoConcedido = remember(estadoPermiso) {
        listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .any { context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }
    }

    // Propiedades para activar la "bolita azul" de ubicación real.
    // Sin remember fijo: debe recomponerse cuando el permiso pasa a concedido.
    val uiSettings = remember { MapUiSettings(myLocationButtonEnabled = false) }
    val properties = remember(permisoConcedido) { MapProperties(isMyLocationEnabled = permisoConcedido) }

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
