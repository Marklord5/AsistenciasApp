package com.kmp.asistencias

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform