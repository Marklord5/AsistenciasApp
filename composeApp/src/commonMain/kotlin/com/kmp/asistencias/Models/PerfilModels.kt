package com.kmp.asistencias.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerfilUsuarioResponse(
    val status: String,
    val message: String,
    val data: Data,
    val traceId: String
) {
    @Serializable
    data class Data(
        val perfil: List<Perfil>,
        val horasHoy: Int,
        val porcentajePuntualidad: Int,
        val ultimosRegistros: List<UltimoRegistro>
    )

    @Serializable
    data class Perfil(
        val nombreCompleto: String,
        val email: String,
        val cargo: String
    )

    @Serializable
    data class UltimoRegistro(
        val tipo: String,
        val fechaHora: String
    )
}