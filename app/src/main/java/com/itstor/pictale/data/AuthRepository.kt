package com.itstor.pictale.data

import com.itstor.pictale.data.local.AuthPreferences
import com.itstor.pictale.data.remote.retrofit.ApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val authPreferences: AuthPreferences
) {
    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun register(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    suspend fun saveAuthToken(token: String) {
        authPreferences.saveAuthToken(token)
    }

    suspend fun clearAuthToken() {
        authPreferences.clearAuthToken()
    }

    fun getAuthToken() = authPreferences.getAuthToken()

    companion object {
        private const val TAG = "AuthRepository"
    }
}