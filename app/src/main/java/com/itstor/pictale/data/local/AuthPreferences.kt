package com.itstor.pictale.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthPreferences @Inject constructor(private val authStore: DataStore<Preferences>) {
    fun getAuthToken() = authStore.data.map { preferences ->
        preferences[AUTH_TOKEN]
    }

    suspend fun saveAuthToken(token: String) {
        authStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

    suspend fun clearAuthToken() {
        authStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
        }
    }

    companion object {
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }
}