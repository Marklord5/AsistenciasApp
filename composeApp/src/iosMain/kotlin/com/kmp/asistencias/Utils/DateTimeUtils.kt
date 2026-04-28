package com.kmp.asistencias.Utils

import platform.Foundation.*

actual fun obtenerFechaActual(): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "EEEE, d 'DE' MMMM"
    formatter.locale = NSLocale("es_ES")
    return formatter.stringFromDate(NSDate()).uppercase()
}

actual fun obtenerHoraActual(): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "HH:mm"
    return formatter.stringFromDate(NSDate())
}
