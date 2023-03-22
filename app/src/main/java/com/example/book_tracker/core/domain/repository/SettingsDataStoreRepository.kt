package com.example.book_tracker.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsDataStoreRepository {
    suspend fun saveSettings(time: Long)
    fun getSettings(): Flow<Long>
}
