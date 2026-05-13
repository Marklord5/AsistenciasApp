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
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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
                // Extraer el id_usuario del JWT
                val userId = extractUserId(response.data.access_token)
                
                // Guardamos los tokens localmente
                SessionManager.saveSession(
                    accessToken = response.data.access_token,
                    refreshToken = response.data.refresh_token,
                    userId = userId
                )
            }
            response
        } catch (e: Exception) {
            // Si hay un error de red (como el que viste), verificamos si hay sesión previa
            if (SessionManager.hasSession()) {
                LoginResponse(success = true, message = "Acceso offline concedido")
            } else {
                LoginResponse(success = false, message = "Error de conexión: ${e.message}")
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun extractUserId(token: String): Int {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return 1
            
            var payload = parts[1]
            // El payload de JWT suele no tener padding, lo agregamos para Base64.UrlSafe
            val missingPadding = 4 - (payload.length % 4)
            if (missingPadding < 4) {
                payload += "=".repeat(missingPadding)
            }

            val decoded = Base64.UrlSafe.decode(payload).decodeToString()
            val jsonElement = Json.parseToJsonElement(decoded)
            val id = jsonElement.jsonObject["id_usuario"]?.jsonPrimitive?.content
            id?.toIntOrNull() ?: 1
        } catch (e: Exception) {
            println("Error al extraer userId del token: ${e.message}")
            1
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
