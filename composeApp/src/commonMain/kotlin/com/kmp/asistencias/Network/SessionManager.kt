package com.kmp.asistencias.Network

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.kmp.asistencias.Models.RequestSincronizacion

object SessionManager {
    private val settings = Settings()
    private val json = Json { ignoreUnknownKeys = true }

    // Keys
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_DATA = "user_data"
    private const val KEY_PENDING_RECORDS = "pending_records"

    fun saveSession(accessToken: String, refreshToken: String, userId: Int) {
        settings[KEY_ACCESS_TOKEN] = accessToken
        settings[KEY_REFRESH_TOKEN] = refreshToken
        settings[KEY_USER_ID] = userId
    }

    fun getAccessToken(): String = settings.getString(KEY_ACCESS_TOKEN, "")
    fun getRefreshToken(): String = settings.getString(KEY_REFRESH_TOKEN, "")
    fun getUserId(): Int = settings.getInt(KEY_USER_ID, -1)

    fun hasSession(): Boolean = getRefreshToken().isNotEmpty()

    fun clearSession() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        settings.remove(KEY_USER_ID)
    }

    // Pending Records Logic
    fun savePendingRecord(record: RequestSincronizacion) {
        val currentRecords = getPendingRecords().toMutableList()
        currentRecords.add(record)
        val jsonString = json.encodeToString(currentRecords)
        settings[KEY_PENDING_RECORDS] = jsonString
    }

    fun getPendingRecords(): List<RequestSincronizacion> {
        val jsonString = settings.getString(KEY_PENDING_RECORDS, "")
        if (jsonString.isEmpty()) return emptyList()
        return try {
            json.decodeFromString(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun clearPendingRecords() {
        settings.remove(KEY_PENDING_RECORDS)
    }
    
    fun removePendingRecord(record: RequestSincronizacion) {
        val currentRecords = getPendingRecords().toMutableList()
        currentRecords.remove(record)
        val jsonString = json.encodeToString(currentRecords)
        settings[KEY_PENDING_RECORDS] = jsonString
    }
}
