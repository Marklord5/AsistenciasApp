package com.kmp.asistencias.Models

import kotlinx.serialization.Serializable


@Serializable
data class ResponseEntradaSalida(
    val status: String,
    val message: String,
    val data: Int,
    val traceId: String

)

@Serializable
data class RequestEntradaSalida (
    val IdUsuario: Int,
    val Latitud: Double,
    val Longitud: Double,
    val UbicacionDetalle: String,
    val Fuente: String
)

@Serializable
data class EncryptedRequest(
    val en: String
)

/*
entrada


"{\"IdUsuario\":1,\"Latitud\":19.432608,\"Longitud\":-99.133209,\"UbicacionDetalle\":\"Sucursal Reforma\",\"Fuente\":\"APP_MOVIL\"}"

salida


"{\"IdUsuario\":1,\"Latitud\":19.432608,\"Longitud\":-99.133209,\"UbicacionDetalle\":\"Sucursal Reforma\",\"Fuente\":\"APP_MOVIL\"}"


 */

