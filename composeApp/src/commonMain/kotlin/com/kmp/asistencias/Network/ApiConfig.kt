package com.kmp.asistencias.Network

object ApiConfig {
    // Cambia esto a true para Producción o false para QA
    private const val IS_PRODUCTION = false

    private const val BASE_URL_QA = "https://qa-asistenciasapi.jorchav.com.mx/api"
    private const val BASE_URL_PROD = "https://asistenciasapi.jorchav.com.mx/api" // Cambia esta URL cuando tengas la de producción

    private val BASE_URL = if (IS_PRODUCTION) BASE_URL_PROD else BASE_URL_QA

    // Endpoints
    val LOGIN = "$BASE_URL/Home/loginMovilMa"
    val REGISTRO_SALIDA = "$BASE_URL/Asistencia/Registro_Salida"
    val REGISTRO_ENTRADA = "$BASE_URL/Asistencia/Registro_Entrada"
    val GET_ACTIVIDAD = "$BASE_URL/Asistencia/GetActividadUsuario"
    val GET_PERFIL = "$BASE_URL/Asistencia/GetPerfilUsuario"
    val GET_FOTO = "$BASE_URL/Asistencia/GetFotoUsuario"
    val UPDATE_FOTO = "$BASE_URL/Asistencia/GuardarDocumentosUsuario"
    val GET_HISTORIAL = "$BASE_URL/Asistencia/GetHistorialUsuario"
}
