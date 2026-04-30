package com.kmp.asistencias.Network

import com.kmp.asistencias.Models.EncryptedRequest
import com.kmp.asistencias.Models.LoginRequest
import com.kmp.asistencias.Models.PerfilUsuarioResponse
import com.kmp.asistencias.Models.RequestEntradaSalida
import com.kmp.asistencias.Models.ResponseEntradaSalida
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

object Home {
    private val settings = Settings()

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun RegistarEntrada(requestEntradaSalida: RequestEntradaSalida, Tipo: Boolean): ResponseEntradaSalida {
        val jsonString = Json.encodeToString(requestEntradaSalida)
        val encryptedData = Crypto.encrypt(jsonString)
        val token = settings.getString("token", "")

        val endpoint: String = if (Tipo) {
            "https://qa-asistenciasapi.jorchav.com.mx/api/Asistencia/Registro_Salida"


        } else {

            "https://qa-asistenciasapi.jorchav.com.mx/api/Asistencia/Registro_Entrada"

        }

        return client.post(endpoint) {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(EncryptedRequest(en = encryptedData))
        }.body()

    }





}




/*
{
    "IdUsuario": 1,
    "Latitud": 19.432608,
    "Longitud": -99.133209,
    "UbicacionDetalle": "Sucursal Reforma",
    "Fuente": "APP_MOVIL"
}
*/
