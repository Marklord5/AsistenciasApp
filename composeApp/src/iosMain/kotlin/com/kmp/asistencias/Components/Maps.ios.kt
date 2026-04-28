package com.kmp.asistencias.Components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GoogleMapView(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    zoom: Float,
    onCameraChange: (Double, Double) -> Unit
) {
    val delegate = remember {
        object : NSObject(), MKMapViewDelegateProtocol {
            override fun mapView(mapView: MKMapView, regionDidChangeAnimated: Boolean) {
                mapView.centerCoordinate.useContents {
                    if (this.latitude != 0.0 && this.longitude != 0.0) {
                        onCameraChange(this.latitude, this.longitude)
                    }
                }
            }
        }
    }

    UIKitView(
        factory = {
            MKMapView().apply {
                this.delegate = delegate
                this.showsUserLocation = true // Activa la "bolita azul" en iOS
                // Inicializar en CDMX si es necesario antes del primer update
                val center = CLLocationCoordinate2DMake(19.4326, -99.1332)
                val region = MKCoordinateRegionMakeWithDistance(center, 1000.0, 1000.0)
                this.setRegion(region, animated = false)
            }
        },
        update = { view ->
            val center = CLLocationCoordinate2DMake(latitude, longitude)
            val region = MKCoordinateRegionMakeWithDistance(center, 1000.0, 1000.0)
            
            val currentCenter = view.centerCoordinate.useContents { this.latitude to this.longitude }
            if (kotlin.math.abs(currentCenter.first - latitude) > 0.0001 ||
                kotlin.math.abs(currentCenter.second - longitude) > 0.0001) {
                view.setRegion(region, animated = true)
            }
            
            // Asegurarnos de que no haya marcadores residuales
            if (view.annotations.isNotEmpty()) {
                view.removeAnnotations(view.annotations)
            }
        },
        modifier = modifier
    )
}
