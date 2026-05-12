package com.kmp.asistencias.Utils

import platform.SystemConfiguration.*
import platform.CoreFoundation.*
import kotlinx.cinterop.*
import platform.posix.sockaddr_in

@OptIn(ExperimentalForeignApi::class)
actual fun isNetworkAvailable(): Boolean {
    memScoped {
        val zeroAddress = alloc<sockaddr_in>()
        zeroAddress.sin_len = sizeOf<sockaddr_in>().toUByte()
        zeroAddress.sin_family = platform.posix.AF_INET.toUByte()

        val reachability = SCNetworkReachabilityCreateWithAddress(null, zeroAddress.ptr.reinterpret()) ?: return false
        val flags = alloc<SCNetworkReachabilityFlagsVar>()
        
        if (!SCNetworkReachabilityGetFlags(reachability, flags.ptr)) return false
        
        val isReachable = (flags.value.toInt() and kSCNetworkFlagsReachable.toInt()) != 0
        val needsConnection = (flags.value.toInt() and kSCNetworkFlagsConnectionRequired.toInt()) != 0
        
        return isReachable && !needsConnection
    }
}
