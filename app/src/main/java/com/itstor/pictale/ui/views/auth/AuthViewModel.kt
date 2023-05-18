package com.itstor.pictale.ui.views.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.itstor.pictale.common.Result
import com.itstor.pictale.data.AuthRepository
import com.itstor.pictale.data.remote.response.GeneralResponse
import com.itstor.pictale.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    suspend fun login(email: String, password: String) = flow {
        emit(Result.loading(null))
        wrapEspressoIdlingResource {
            try {
                val response = authRepository.login(email, password)

                if (response.loginResult?.token != null) {
                    authRepository.saveAuthToken(response.loginResult.token)
                    emit(Result.success(null))

                    return@flow
                }

                emit(Result.error(null, "Unknown error"))
            } catch (e: Exception) {
                if (e is HttpException) {
                    val errorResponse = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(errorResponse, GeneralResponse::class.java)
                    emit(Result.error(null, errorBody.message))
                    return@flow
                }
                Log.d(TAG, "login error: ${e.message}")
                emit(Result.error(null, e.message))
            }
        }
    }

    suspend fun register(name: String, email: String, password: String) = flow {
        emit(Result.loading(null))
        try {
            authRepository.register(name, email, password)
            emit(Result.success(null))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(errorResponse, GeneralResponse::class.java)
                emit(Result.error(null, errorBody.message))
                return@flow
            }
            Log.d(TAG, "register error: ${e.message}")
            emit(Result.error(null, e.message))

        }
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}