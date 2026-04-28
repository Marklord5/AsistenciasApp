package com.kmp.asistencias.Network

import kotlinx.cinterop.*
import platform.CoreCrypto.*
import platform.Foundation.*
import platform.posix.size_tVar

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual object Crypto {
    private const val KEY_BASE64 = "QALhwu+oXB+xZTl95uQWJ1GtcuIbcRIlLuYfwPRmpbA="
    private const val IV_BASE64 = "EXyIvqE9Hov5MRFbi5QYEg=="

    actual fun encrypt(text: String): String {
        val nsString = NSString.create(string = text)
        val data = nsString.dataUsingEncoding(NSUTF8StringEncoding) ?: return ""
        val keyData = NSData.create(base64EncodedString = KEY_BASE64, options = 0u) ?: return ""
        val ivData = NSData.create(base64EncodedString = IV_BASE64, options = 0u) ?: return ""

        val encryptedData = crypt(data, keyData, ivData, kCCEncrypt) ?: return ""
        return encryptedData.base64EncodedStringWithOptions(0u)
    }

    actual fun decrypt(encryptedText: String): String {
        val data = NSData.create(base64EncodedString = encryptedText, options = 0u) ?: return ""
        val keyData = NSData.create(base64EncodedString = KEY_BASE64, options = 0u) ?: return ""
        val ivData = NSData.create(base64EncodedString = IV_BASE64, options = 0u) ?: return ""

        val decryptedData = crypt(data, keyData, ivData, kCCDecrypt) ?: return ""
        return NSString.create(data = decryptedData, encoding = NSUTF8StringEncoding)?.toString() ?: ""
    }

    private fun crypt(data: NSData, keyData: NSData, ivData: NSData, operation: CCOperation): NSData? = memScoped {
        val outLength = data.length + kCCBlockSizeAES128.toULong()
        val outBuffer = allocArray<ByteVar>(outLength.toLong())
        val numBytesCrypted = alloc<size_tVar>()

        val status = CCCrypt(
            operation,
            kCCAlgorithmAES,
            kCCOptionPKCS7Padding,
            keyData.bytes, keyData.length,
            ivData.bytes,
            data.bytes, data.length,
            outBuffer, outLength,
            numBytesCrypted.ptr
        )

        return if (status == kCCSuccess) {
            NSData.create(bytes = outBuffer, length = numBytesCrypted.value)
        } else {
            null
        }
    }
}
