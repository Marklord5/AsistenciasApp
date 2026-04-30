package com.kmp.asistencias.Network

import com.kmp.asistencias.Models.ResponseActividaUsuario
import com.kmp.asistencias.Network.Home.token
import com.kmp.asistencias.Models.HistorialResponse
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Historial {
    private val settings = Settings()
    val token = settings.getString("token", "")

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun Historial(): HistorialResponse {
        return client.get("https://qa-asistenciasapi.jorchav.com.mx/api/Asistencia/GetHistorialUsuario") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
        }.body()
    }
}