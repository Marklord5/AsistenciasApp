package com.kmp.asistencias.Utils

import dev.jordond.compass.Priority
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.Locator
import dev.jordond.compass.geolocation.mobile.mobile
import kotlinx.coroutines.flow.MutableStateFlow

// Instancia única para toda la app: evita que dos peticiones simultáneas
// (mapa y home) entren en conflicto y reporten mal el estado del permiso en iOS.
object LocationProvider {
    val geolocator: Geolocator by lazy { Geolocator(Locator.mobile()) }

    // null = aún sin verificar, true = ubicación obtenida, false = permiso negado o localización apagada
    val permisoUbicacion = MutableStateFlow<Boolean?>(null)

    // Único punto para pedir la ubicación: actualiza el estado del permiso según el resultado real
    suspend fun obtenerUbicacion(priority: Priority = Priority.Balanced): GeolocatorResult {
        val resultado = geolocator.current(priority)
        println("LocationProvider: $resultado")
        when (resultado) {
            is GeolocatorResult.Success -> permisoUbicacion.value = true
            is GeolocatorResult.PermissionDenied,
            is GeolocatorResult.NotSupported -> permisoUbicacion.value = false
            else -> {} // otros errores (sin señal GPS, etc.) no son de permisos
        }
        return resultado
    }
}
