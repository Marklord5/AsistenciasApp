package com.kmp.asistencias.Utils

import java.text.SimpleDateFormat
import java.util.*

actual fun obtenerFechaActual(): String {
    val sdf = SimpleDateFormat("EEEE, d 'DE' MMMM", Locale("es", "ES"))
    return sdf.format(Date()).uppercase()
}

actual fun obtenerHoraActual(): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date())
}
