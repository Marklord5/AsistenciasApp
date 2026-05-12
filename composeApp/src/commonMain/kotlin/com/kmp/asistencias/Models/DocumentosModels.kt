package com.kmp.asistencias.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocsUsuarioResponse(
    @SerialName("status")
    val status: String,

    @SerialName("message")
    val message: String,

    @SerialName("data")
    val data: List<DocumentoUsuario>,

    @SerialName("traceId")
    val traceId: String
)

@Serializable
data class DocumentoUsuario(
    @SerialName("nombre_Archivo")
    val nombreArchivo: String,

    @SerialName("extension")
    val extension: String,

    @SerialName("tipo_Documento")
    val tipoDocumento: String,

    @SerialName("url_Archivo")
    val urlArchivo: String
)