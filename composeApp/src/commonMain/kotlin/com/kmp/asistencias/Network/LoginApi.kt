package com.kmp.asistencias.Network

import com.kmp.asistencias.Models.LoginRequest
import com.kmp.asistencias.Models.LoginResponse
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

    suspend fun login(email: String, pass: String): LoginResponse = 
        client.post("https://qa-asistenciasapi.jorchav.com.mx/api/Home/loginMovilMa") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, pass))
        }.body()
}
