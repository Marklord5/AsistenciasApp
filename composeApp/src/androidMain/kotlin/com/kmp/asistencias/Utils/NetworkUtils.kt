package com.kmp.asistencias.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.kmp.asistencias.MainActivity

// Esta es una forma simple de obtener el contexto en un proyecto KMP 
// Se asume que MainActivity guarda una referencia estática o similar si no hay DI
actual fun isNetworkAvailable(): Boolean {
    val context = MainActivity.context ?: return true // Fallback a true si no hay contexto
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}
