package com.kmp.asistencias.Network

import com.kmp.asistencias.Models.DocsUsuarioResponse
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

object Documentos {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val settings = Settings()
    private val token = settings.getString("token", "")

    suspend fun GetListaDomcumentos(): DocsUsuarioResponse {
        return client.get(ApiConfig.GET_DOCUMENTOS_LIST) {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
        }.body()
    }
}
