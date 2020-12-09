package com.github.margawron.epidemicalertapp.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.api.ApiResponse
import com.github.margawron.epidemicalertapp.auth.AuthService
import com.github.margawron.epidemicalertapp.auth.LoginRequest
import com.github.margawron.epidemicalertapp.util.PreferenceHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel @ViewModelInject internal constructor(
    preferenceHelper: PreferenceHelper,
    private val authService: AuthService,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    val navigateToRegisterViewModel = MutableLiveData(false)
    var login = preferenceHelper.getLastLoggedUsername()
    var password = preferenceHelper.getLastLoggedPassword()
    var rememberPassword = preferenceHelper.getShouldRememberPassword()

    fun onLoginClick() {
        viewModelScope.launch {
            val token = withContext(Dispatchers.IO) {
                authService.getBearerToken(
                    LoginRequest(
                        login,
                        password
                    )
                )
            }
            if(token is ApiResponse.SuccessWithBody) {
                Toast.makeText(context, token.body.accessToken, Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun onRegisterClick() {
        navigateToRegisterViewModel.value = true
    }

}