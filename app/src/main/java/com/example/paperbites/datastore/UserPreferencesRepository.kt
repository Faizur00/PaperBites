package com.example.paperbites.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDate

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class FilterSettings(
    val fieldId: String = "cs",
    val subfieldIds: Set<String> = emptySet(),
    val fromYear: Int = LocalDate.now().year - 5,
    val toYear: Int = LocalDate.now().year
)

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val FILTER_FIELD_ID = stringPreferencesKey("filter_field_id")
        val FILTER_SUBFIELD_IDS = stringSetPreferencesKey("filter_subfield_ids")
        val FILTER_FROM_YEAR = intPreferencesKey("filter_from_year")
        val FILTER_TO_YEAR = intPreferencesKey("filter_to_year")
    }

    val isDarkModeFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] ?: false
        }

    val filterSettingsFlow: Flow<FilterSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            FilterSettings(
                fieldId = preferences[PreferencesKeys.FILTER_FIELD_ID] ?: "cs",
                subfieldIds = preferences[PreferencesKeys.FILTER_SUBFIELD_IDS] ?: emptySet(),
                fromYear = preferences[PreferencesKeys.FILTER_FROM_YEAR] ?: (LocalDate.now().year - 5),
                toYear = preferences[PreferencesKeys.FILTER_TO_YEAR] ?: LocalDate.now().year
            )
        }

    suspend fun updateDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode
        }
    }

    suspend fun updateFilterSettings(settings: FilterSettings) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FILTER_FIELD_ID] = settings.fieldId
            preferences[PreferencesKeys.FILTER_SUBFIELD_IDS] = settings.subfieldIds
            preferences[PreferencesKeys.FILTER_FROM_YEAR] = settings.fromYear
            preferences[PreferencesKeys.FILTER_TO_YEAR] = settings.toYear
        }
    }

    suspend fun resetFilterSettings() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.FILTER_FIELD_ID)
            preferences.remove(PreferencesKeys.FILTER_SUBFIELD_IDS)
            preferences.remove(PreferencesKeys.FILTER_FROM_YEAR)
            preferences.remove(PreferencesKeys.FILTER_TO_YEAR)
        }
    }

    suspend fun fetchCurrentFilters(): FilterSettings = filterSettingsFlow.first()
}
