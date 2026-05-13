package com.kmp.asistencias.Network

import com.kmp.asistencias.Models.ResponseActividaUsuario
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
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val token: String
        get() = SessionManager.getAccessToken()

    suspend fun Historial(): HistorialResponse {
        return client.get(ApiConfig.GET_HISTORIAL) {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
        }.body()
    }
}