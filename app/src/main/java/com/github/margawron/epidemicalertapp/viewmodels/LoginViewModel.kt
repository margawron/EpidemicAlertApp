package com.github.margawron.epidemicalertapp.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.LocationDisplayActivity
import com.github.margawron.epidemicalertapp.RegisterActivity
import com.github.margawron.epidemicalertapp.api.ApiResponse
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.auth.LoginRequest
import com.github.margawron.epidemicalertapp.service.LocationForegroundService
import com.github.margawron.epidemicalertapp.util.PreferenceHelper
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder


class LoginViewModel @ViewModelInject internal constructor(
    private val preferenceHelper: PreferenceHelper,
    private val authManager: AuthManager,
    @ActivityContext private val appContext: Context,
) : ViewModel() {
    var login = preferenceHelper.getLastLoggedUsername()
    var password = preferenceHelper.getLastLoggedPassword()
    var rememberPassword = preferenceHelper.getShouldRememberPassword()

    init {
        if(preferenceHelper.getShouldRememberPassword()){
            onLoginClick()
        }
    }


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
                is ApiResponse.Success -> {
                    checkIfShouldRememberPassword()
                    val foregroundIntent = Intent(appContext, LocationForegroundService::class.java)
                    appContext.startForegroundService(foregroundIntent)
                    val locationDisplayIntent = Intent(appContext, LocationDisplayActivity::class.java)
                    locationDisplayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    appContext.startActivity(locationDisplayIntent)
                }
                is ApiResponse.Error -> {
                    val errorBuilder = StringBuilder()
                    response.errors.joinTo(errorBuilder, postfix = "\n") { e -> e.errorMessage }
                    Toast.makeText(appContext, errorBuilder, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkIfShouldRememberPassword() {
        if (rememberPassword) {
            preferenceHelper.setLastLoggedUsername(login)
            preferenceHelper.setLastLoggedPassword(password)
            preferenceHelper.setShouldRememberPassword(true)
        } else {
            preferenceHelper.setLastLoggedUsername("")
            preferenceHelper.setLastLoggedPassword("")
            preferenceHelper.setShouldRememberPassword(false)
        }
    }

    fun onRegisterClick() {
        appContext.startActivity(Intent(appContext, RegisterActivity::class.java))
    }

}