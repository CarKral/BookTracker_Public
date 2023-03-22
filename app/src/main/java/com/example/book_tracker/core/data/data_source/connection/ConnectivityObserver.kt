package com.example.book_tracker.core.data.data_source.connection

import kotlinx.coroutines.flow.Flow


interface ConnectivityObserver {
    fun observe(): Flow<ConnectionState>

    enum class ConnectionState {
        Available,
        Unavailable
    }
}