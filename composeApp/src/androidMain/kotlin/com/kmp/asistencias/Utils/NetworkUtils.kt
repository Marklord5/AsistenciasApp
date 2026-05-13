package com.kmp.asistencias.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.kmp.asistencias.MainActivity

// Esta es una forma simple de obtener el contexto en un proyecto KMP 
// Se asume que MainActivity guarda una referencia estática o similar si no hay DI
actual fun isNetworkAvailable(): Boolean {
    val context = MainActivity.context ?: return true 
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
           capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
