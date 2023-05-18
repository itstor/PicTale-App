package com.itstor.pictale.ui.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itstor.pictale.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val authRepo: AuthRepository) : ViewModel() {
    fun getAuthToken() = authRepo.getAuthToken()

    fun logout() {
        viewModelScope.launch {
            authRepo.clearAuthToken()
        }
    }
}