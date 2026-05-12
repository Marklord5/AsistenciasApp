package com.kmp.asistencias.Network

import com.kmp.asistencias.Models.EncryptedRequest
import com.kmp.asistencias.Models.EncryptedSyncRequest
import com.kmp.asistencias.Models.LoginRequest
import com.kmp.asistencias.Models.PerfilUsuarioResponse
import com.kmp.asistencias.Models.RequestEntradaSalida
import com.kmp.asistencias.Models.RequestSincronizacion
import com.kmp.asistencias.Models.ResponseActividaUsuario
import com.kmp.asistencias.Models.ResponseEntradaSalida
import com.kmp.asistencias.Utils.isNetworkAvailable
import com.kmp.asistencias.Utils.obtenerFechaActual
import com.kmp.asistencias.Utils.obtenerHoraActual
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.kmp.asistencias.Network.Crypto
import kotlinx.coroutines.delay

object Home {
    private val settings = Settings()
    private val token: String
        get() = SessionManager.getAccessToken()

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun RegistarEntrada(requestEntradaSalida: RequestEntradaSalida, Tipo: Boolean): ResponseEntradaSalida {
        if (!isNetworkAvailable()) {
            val tipoStr = if (Tipo) "SALIDA" else "ENTRADA"
            val fechaHora = "${obtenerFechaActual()}T${obtenerHoraActual()}"
            
            val pendingRecord = RequestSincronizacion(
                IdUsuario = requestEntradaSalida.IdUsuario,
                Tipo = tipoStr,
                FechaHora = fechaHora,
                Latitud = requestEntradaSalida.Latitud,
                Longitud = requestEntradaSalida.Longitud,
                UbicacionDetalle = requestEntradaSalida.UbicacionDetalle,
                Fuente = requestEntradaSalida.Fuente
            )
            
            SessionManager.savePendingRecord(pendingRecord)
            
            return ResponseEntradaSalida(
                status = "Offline",
                message = "Guardado localmente por falta de internet.",
                data = 0,
                traceId = ""
            )
        }

        // Si hay internet, intentamos sincronizar pendientes primero para mantener orden
        SincronizarPendientes()

        val jsonString = Json.encodeToString(requestEntradaSalida)
        val encryptedData = Crypto.encrypt(jsonString)

        val endpoint: String = if (Tipo) {
            ApiConfig.REGISTRO_SALIDA
        } else {
            ApiConfig.REGISTRO_ENTRADA
        }

        return client.post(endpoint) {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(EncryptedRequest(en = encryptedData))
        }.body()

    }

    suspend fun ActividadUsuario(): ResponseActividaUsuario {
        if (!isNetworkAvailable()) {
            return ResponseActividaUsuario(
                status = "Error",
                message = "Sin conexión a internet.",
                data = emptyList(),
                traceId = ""
            )
        }
        return client.get(ApiConfig.GET_ACTIVIDAD) {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun SincronizarPendientes(): List<String> {
        val pendingRecords = SessionManager.getPendingRecords()
        val results = mutableListOf<String>()

        if (!isNetworkAvailable() || pendingRecords.isEmpty()) return results

        // Pequeña espera por si el internet acaba de volver
        delay(1000)

        for (record in pendingRecords) {
            try {
                val jsonString = Json.encodeToString(record)
                val encryptedData = Crypto.encrypt(jsonString)

                val response: ResponseEntradaSalida = client.post(ApiConfig.REGISTRO_SINCRONIZACION) {
                    header("Authorization", "Bearer $token")
                    contentType(ContentType.Application.Json)
                    setBody(EncryptedSyncRequest(en = encryptedData))
                }.body()

                if (response.status == "Success") {
                    SessionManager.removePendingRecord(record)
                    results.add("Sincronizado: ${record.Tipo} - ${record.FechaHora}")
                } else {
                    results.add("Error al sincronizar: ${response.message}")
                }
            } catch (e: Exception) {
                results.add("Excepción al sincronizar: ${e.message}")
            }
        }
        return results
    }
}
