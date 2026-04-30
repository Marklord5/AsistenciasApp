package com.kmp.asistencias.Models

import kotlinx.serialization.SerialName
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




@Serializable
data class ResponseActividaUsuario(
    val status: String,
    val message: String,
    val data: List<ActividadUsuario>,
    val traceId: String

)
@Serializable
data class ActividadUsuario(
    @SerialName("fechA_CREACION")
    val fechaCreacion: String,

    @SerialName("nombrE_DIA")
    val nombreDia: String,

    @SerialName("horA_24H")
    val hora24h: String,

    @SerialName("etiquetA_FECHA")
    val etiquetaFecha: String,

    val tipo: String
)
