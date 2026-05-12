package com.kmp.asistencias.Utils

import kotlinx.coroutines.flow.Flow

expect object NetworkMonitor {
    val isOnline: Flow<Boolean>
}
