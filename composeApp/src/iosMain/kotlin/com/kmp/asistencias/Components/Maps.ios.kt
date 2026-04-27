package com.kmp.asistencias.Components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMapView(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    zoom: Float
) {
    UIKitView(
        factory = {
            MKMapView()
        },
        update = { view ->
            val center = CLLocationCoordinate2DMake(latitude, longitude)
            val region = MKCoordinateRegionMakeWithDistance(center, 1000.0, 1000.0)
            view.setRegion(region, animated = true)
            
            // Limpiar y añadir marcador
            view.removeAnnotations(view.annotations)
            val annotation = MKPointAnnotation().apply {
                setCoordinate(center)
                setTitle("Tu ubicación")
            }
            view.addAnnotation(annotation)
        },
        modifier = modifier
    )
}
