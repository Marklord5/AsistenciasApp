package com.kmp.asistencias.Network

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

actual object Crypto {
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val KEY_BASE64 = "QALhwu+oXB+xZTl95uQWJ1GtcuIbcRIlLuYfwPRmpbA="
    private const val IV_BASE64 = "EXyIvqE9Hov5MRFbi5QYEg=="

    private val keyBytes = Base64.decode(KEY_BASE64, Base64.DEFAULT)
    private val ivBytes = Base64.decode(IV_BASE64, Base64.DEFAULT)

    actual fun encrypt(text: String): String {
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val ivSpec = IvParameterSpec(ivBytes)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encrypted = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
            Base64.encodeToString(encrypted, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    actual fun decrypt(encryptedText: String): String {
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val ivSpec = IvParameterSpec(ivBytes)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decodedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
            val decrypted = cipher.doFinal(decodedBytes)
            String(decrypted, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
