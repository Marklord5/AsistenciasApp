package com.kmp.asistencias.Utils

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Network.*
import platform.darwin.dispatch_get_main_queue

actual object NetworkMonitor {
    actual val isOnline: Flow<Boolean> = callbackFlow {
        val monitor = nw_path_monitor_create()
        
        nw_path_monitor_set_update_handler(monitor) { path ->
            val status = nw_path_get_status(path)
            trySend(status == nw_path_status_satisfied)
        }
        
        nw_path_monitor_start(monitor)
        
        awaitClose {
            nw_path_monitor_cancel(monitor)
        }
    }
}
