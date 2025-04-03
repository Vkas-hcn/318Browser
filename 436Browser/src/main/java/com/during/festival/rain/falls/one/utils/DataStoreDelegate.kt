package com.during.festival.rain.falls.one.utils


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KProperty

// Extension property for singleton DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_preferences")
class DataStoreDelegate<T>(
    private val context: Context,
    private val key: String,
    private val defaultValue: T
) {
    // Convert the key string to a Preferences.Key<T>
    private val preferenceKey: Preferences.Key<T> = when (defaultValue) {
        is Boolean -> booleanPreferencesKey(key) as Preferences.Key<T>
        is Int -> intPreferencesKey(key) as Preferences.Key<T>
        is Long -> longPreferencesKey(key) as Preferences.Key<T>
        is Float -> floatPreferencesKey(key) as Preferences.Key<T>
        is String -> stringPreferencesKey(key) as Preferences.Key<T>
        else -> throw IllegalArgumentException("Unsupported type")
    }

    // Flow to observe changes to this preference
    fun asFlow(): Flow<T> {
        return context.dataStore.data.map { preferences ->
            preferences[preferenceKey] ?: defaultValue
        }
    }

    // For property delegate get operation - blocking for compatibility
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[preferenceKey] ?: defaultValue
            }.first()
        }
    }

    // For property delegate set operation - blocking for compatibility
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[preferenceKey] = value
            }
        }
    }

    // Optional: Suspend functions for coroutine usage
    suspend fun get(): T {
        return context.dataStore.data.map { preferences ->
            preferences[preferenceKey] ?: defaultValue
        }.first()
    }

    suspend fun set(value: T) {
        context.dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

}