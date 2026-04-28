package com.kmp.asistencias.Services

import com.kmp.asistencias.Models.PerfilUsuarioResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json

object Perfil {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun getPerfil(token: String): PerfilUsuarioResponse =
        client.get("https://qa-asistenciasapi.jorchav.com.mx/api/Asistencia/GetPerfilUsuario") {
            header("Authorization", "Bearer $token")
            accept(ContentType.Application.Json)
        }.body()
}