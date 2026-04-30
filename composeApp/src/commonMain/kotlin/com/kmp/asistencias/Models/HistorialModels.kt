package com.kmp.asistencias.Models

import kotlinx.serialization.Serializable

@Serializable
data class HistorialResponse(
    val status: String,
    val message: String,
    val data: HistorialData,
    val traceId: String
)

@Serializable
data class HistorialData(
    val detallePorDia: List<DetallePorDia>,
    val totalHoras: Double,
    val asistenciaPerfectaConsecutiva: Int
)

@Serializable
data class DetallePorDia(
    val fecha: String,
    val horaEntrada: String?,
    val horaSalida: String?,
    val horasTrabajadas: Double
)
