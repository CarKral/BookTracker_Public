package com.example.book_tracker.core.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.book_tracker.core.domain.repository.WelcomeStateDataStoreRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class WelcomeStateDataStoreRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    WelcomeStateDataStoreRepository {

    companion object {
        const val WELCOME_PREFERENCES_NAME = "welcome_preferences"
        private val Context.dataStore by preferencesDataStore(WELCOME_PREFERENCES_NAME)
    }


    private object PreferencesKey {
        val welcomeKey = booleanPreferencesKey(name = "welcome_completed")
    }

    private val dataStore = context.dataStore

    override suspend fun saveWelcomeState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.welcomeKey] = completed
        }
    }

    override fun getWelcomeState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                val welcomeState = preferences[PreferencesKey.welcomeKey] ?: false
                welcomeState
            }

    }
}