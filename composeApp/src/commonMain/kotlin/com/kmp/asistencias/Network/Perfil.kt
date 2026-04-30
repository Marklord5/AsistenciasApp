package com.kmp.asistencias.Services

import com.kmp.asistencias.Models.FotoPerfil
import com.kmp.asistencias.Models.LoginRequest
import com.kmp.asistencias.Models.LoginResponse
import com.kmp.asistencias.Models.PerfilUsuarioResponse
import com.kmp.asistencias.Models.RequestFoto
import com.kmp.asistencias.Models.Documento
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import com.kmp.asistencias.Network.ApiConfig
import com.kmp.asistencias.Network.LoginApi
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.contentType

object Perfil {

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

    suspend fun getPerfil(): PerfilUsuarioResponse {


        return client.get(ApiConfig.GET_PERFIL) {
            header("Authorization", "Bearer $token")
            accept(ContentType.Application.Json)
        }.body()
    }

    suspend fun ObtenerFoto(): FotoPerfil {


        return client.get(ApiConfig.GET_FOTO) {
            header("Authorization", "Bearer $token")
            accept(ContentType.Application.Json)
        }.body()
    }



    suspend fun CambiarFoto(request: RequestFoto): LoginResponse {

        return client.post(ApiConfig.UPDATE_FOTO) {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }


}