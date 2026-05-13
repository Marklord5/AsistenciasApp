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
import com.kmp.asistencias.Utils.obtenerFechaHoraISO
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

    private var isSyncing = false

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun RegistarEntrada(requestEntradaSalida: RequestEntradaSalida, Tipo: Boolean): ResponseEntradaSalida {
        val tipoStr = if (Tipo) "SALIDA" else "ENTRADA"
        
        if (!isNetworkAvailable()) {
            val pendingRecord = RequestSincronizacion(
                IdUsuario = requestEntradaSalida.IdUsuario,
                Tipo = tipoStr,
                FechaHora = obtenerFechaHoraISO(),
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

        return try {
            val response: ResponseEntradaSalida = client.post(endpoint) {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(EncryptedRequest(en = encryptedData))
            }.body()
            response
        } catch (e: Exception) {
            println("Error al registrar (intentando guardar local): ${e.message}")
            
            val pendingRecord = RequestSincronizacion(
                IdUsuario = requestEntradaSalida.IdUsuario,
                Tipo = tipoStr,
                FechaHora = obtenerFechaHoraISO(),
                Latitud = requestEntradaSalida.Latitud,
                Longitud = requestEntradaSalida.Longitud,
                UbicacionDetalle = requestEntradaSalida.UbicacionDetalle,
                Fuente = requestEntradaSalida.Fuente
            )
            
            SessionManager.savePendingRecord(pendingRecord)
            
            ResponseEntradaSalida(
                status = "Offline",
                message = "Error de red, se guardó localmente.",
                data = 0,
                traceId = ""
            )
        }
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
        if (isSyncing) return emptyList()
        
        val pendingRecords = SessionManager.getPendingRecords()
        if (pendingRecords.isEmpty()) return emptyList()

        val results = mutableListOf<String>()
        isSyncing = true
        
        try {
            println("SincronizarPendientes: Iniciando sincronización de ${pendingRecords.size} registros")
            
            // Esperar un poco para asegurar que la conexión sea estable si acaba de volver
            delay(1500)

            for (record in pendingRecords) {
                try {
                    val jsonString = Json.encodeToString(record)
                    val encryptedData = Crypto.encrypt(jsonString)

                    println("Sincronizando registro: ${record.Tipo} - ${record.FechaHora}")

                    val response: ResponseEntradaSalida = client.post(ApiConfig.REGISTRO_SINCRONIZACION) {
                        header("Authorization", "Bearer $token")
                        contentType(ContentType.Application.Json)
                        setBody(EncryptedSyncRequest(en = encryptedData))
                    }.body()

                    if (response.status == "Success") {
                        results.add("Sincronizado: ${record.Tipo} - ${record.FechaHora}")
                        println("Sincronización exitosa para: ${record.Tipo}")
                    } else {
                        results.add("Error al sincronizar: ${response.message}")
                        println("Error del servidor al sincronizar: ${response.message}")
                    }
                } catch (e: Exception) {
                    results.add("Excepción al sincronizar: ${e.message}")
                    println("Excepción al sincronizar registro: ${e.message}")
                    if (!isNetworkAvailable()) break
                }
            }
            // LA SOLUCIÓN QUE BUSCAS: Borrar todo lo local de un plumazo al terminar el ciclo
            SessionManager.clearPendingRecords()
        } finally {
            isSyncing = false
        }
        return results
    }
}
