package com.kmp.asistencias.Models

import asistencias.composeapp.generated.resources.Res
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
        val horasHoy: String,
        val porcentajePuntualidad: Double,
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
        @SerialName("fechA_HORA")
        val fechaHora: String
    )
}


@Serializable
data class FotoPerfil(
    val status: String,
    val message: String,
    val data: String,
    val traceId: String

)



@Serializable
data class RequestFoto(
    val idUsuario: Int,
    val documento: Documento
)

@Serializable
data class Documento(
    val idTipoDocumento: Int,
    val base64: String,
    val extension: String
)