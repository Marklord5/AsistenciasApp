package com.kmp.asistencias.Models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val data: Tokens
) {
    @Serializable
    data class Tokens(
        val access_token: String,
        val refresh_token: String
    )
}
