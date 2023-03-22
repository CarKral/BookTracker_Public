package com.example.book_tracker.core.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.book_tracker.core.domain.repository.SettingsDataStoreRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

const val SETTINGS_PREFERENCES_NAME = "settings_preferences"

class SettingsDataStoreRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    SettingsDataStoreRepository {

    private val Context.dataStore by preferencesDataStore(SETTINGS_PREFERENCES_NAME)

    private object PreferencesKey {
        val alarmTimeKey = longPreferencesKey(name = "alarm_time")
    }

    private val dataStore = context.dataStore


    override suspend fun saveSettings(time: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.alarmTimeKey] = time
        }
    }

    override fun getSettings(): Flow<Long> {

        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                val settingsState = preferences[PreferencesKey.alarmTimeKey] ?: 0L
                settingsState
            }
    }

//    override suspend fun saveWelcomeState(completed: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[PreferencesKey.welcomeKey] = completed
//        }
//    }
//
//    override fun getWelcomeState(): Flow<Boolean> {
//        return dataStore.data
//            .catch { exception ->
//                if (exception is IOException) emit(emptyPreferences())
//                else throw exception
//            }
//            .map { preferences ->
//                val welcomeState = preferences[PreferencesKey.welcomeKey] ?: false
//                welcomeState
//            }
//
//    }
}