package com.example.book_tracker.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface WelcomeStateDataStoreRepository {
    suspend fun saveWelcomeState(completed: Boolean)
    fun getWelcomeState(): Flow<Boolean>
}
