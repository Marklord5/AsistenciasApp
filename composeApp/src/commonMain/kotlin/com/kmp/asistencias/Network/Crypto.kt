package com.kmp.asistencias.Network

expect object Crypto {
    fun encrypt(text: String): String
    fun decrypt(encryptedText: String): String
}
