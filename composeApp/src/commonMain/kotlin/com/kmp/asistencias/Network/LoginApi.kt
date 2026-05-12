package com.kmp.asistencias.Network

import com.kmp.asistencias.Models.LoginRequest
import com.kmp.asistencias.Models.LoginResponse
import com.kmp.asistencias.Utils.isNetworkAvailable
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object LoginApi {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun login(email: String, pass: String): LoginResponse {
        if (!isNetworkAvailable()) {
            // Lógica Offline: Si ya tiene sesión, permitimos entrar (Simulado)
            return if (SessionManager.hasSession()) {
                LoginResponse(success = true, message = "Acceso offline concedido")
            } else {
                LoginResponse(success = false, message = "Sin internet y sin sesión previa")
            }
        }

        return try {
            val response: LoginResponse = client.post(ApiConfig.LOGIN) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, pass))
            }.body()

            if (response.success && response.data != null) {
                // Guardamos los tokens localmente
                SessionManager.saveSession(
                    accessToken = response.data.access_token,
                    refreshToken = response.data.refresh_token,
                    userId = 1 // Idealmente obtenerlo de la respuesta si viene ahí
                )
            }
            response
        } catch (e: Exception) {
            LoginResponse(success = false, message = "Error de conexión: ${e.message}")
        }
    }

    suspend fun refreshAccessToken(): Boolean {
        if (!isNetworkAvailable()) return false
        val refreshToken = SessionManager.getRefreshToken()
        if (refreshToken.isEmpty()) return false

        return try {
            // Aquí iría el endpoint de refresh si existiera
            // val response = client.post(ApiConfig.REFRESH_TOKEN) { ... }
            true 
        } catch (e: Exception) {
            false
        }
    }
}
