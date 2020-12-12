package com.github.margawron.epidemicalertapp.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.api.ApiResponse
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.auth.LoginRequest
import com.github.margawron.epidemicalertapp.service.LocationForegroundService
import com.github.margawron.epidemicalertapp.util.PreferenceHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel @ViewModelInject internal constructor(
    preferenceHelper: PreferenceHelper,
    private val authManager: AuthManager,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {
    val navigateToRegisterViewModel = MutableLiveData(false)
    var login = preferenceHelper.getLastLoggedUsername()
    var password = preferenceHelper.getLastLoggedPassword()
    var rememberPassword = preferenceHelper.getShouldRememberPassword()

    fun onLoginClick() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                return@withContext authManager.loginUser(
                    LoginRequest(
                        login,
                        password
                    )
                )
            }
            when (response) {
                is ApiResponse.SuccessWithBody -> {
                    Toast.makeText(
                        appContext,
                        response.body.accessToken,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ApiResponse.SuccessWithoutBody -> {
                    Toast.makeText(appContext, response.message, Toast.LENGTH_SHORT).show()
                }
                is ApiResponse.Error -> {
                    Toast.makeText(appContext, response.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
            val foregroundIntent = Intent(appContext, LocationForegroundService::class.java)
            appContext.startForegroundService(foregroundIntent)
        }
    }

    fun onRegisterClick() {
        navigateToRegisterViewModel.value = true
    }

}