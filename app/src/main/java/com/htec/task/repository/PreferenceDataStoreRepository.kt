package com.htec.task.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

const val PREF_NAME = "htec-pref"

class PreferenceDataStore(context : Context) {
    private object PreferenceKeys {
        val LAST_UPDATE_TIME = preferencesKey<Long>("LastUpdateTime")
    }

    private val dataStore : DataStore<Preferences> = context.createDataStore(PREF_NAME)

    suspend fun saveCurrentTime() {
        dataStore.edit { preference ->
            preference[PreferenceKeys.LAST_UPDATE_TIME] = System.currentTimeMillis()
        }
    }

    val readLastUpdateTime : Flow<Long> = dataStore.data
        .catch { e ->
            if(e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
        .map { preference ->
            preference[PreferenceKeys.LAST_UPDATE_TIME] ?: 0
        }
}