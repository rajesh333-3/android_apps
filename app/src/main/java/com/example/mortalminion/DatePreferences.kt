package com.example.mortalminion

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneOffset

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DatePreferences(private val context: Context) {
    private val BIRTH_DATE = longPreferencesKey("birth_date")

    suspend fun saveBirthDate(date: LocalDate) {
        context.dataStore.edit { preferences ->
            preferences[BIRTH_DATE] = date.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
        }
    }

    val birthDate: Flow<LocalDate?> = context.dataStore.data.map { preferences ->
        preferences[BIRTH_DATE]?.let { timestamp ->
            LocalDate.ofEpochDay(timestamp / 86400)
        }
    }
} 