package com.itstor.pictale.ui.views.splash

import androidx.lifecycle.ViewModel
import com.itstor.pictale.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val authRepo: AuthRepository) : ViewModel() {
    fun getAuthToken() = authRepo.getAuthToken()
}