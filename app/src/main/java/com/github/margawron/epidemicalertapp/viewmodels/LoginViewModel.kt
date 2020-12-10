package com.github.margawron.epidemicalertapp.viewmodels

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.auth.LoginRequest
import com.github.margawron.epidemicalertapp.util.PreferenceHelper
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel @ViewModelInject internal constructor(
    preferenceHelper: PreferenceHelper,
    private val authManager: AuthManager,
    @ActivityContext private val activityContext: Context,
) : ViewModel() {
    val navigateToRegisterViewModel = MutableLiveData(false)
    var login = preferenceHelper.getLastLoggedUsername()
    var password = preferenceHelper.getLastLoggedPassword()
    var rememberPassword = preferenceHelper.getShouldRememberPassword()

    fun onLoginClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val isSuccessful =  authManager.loginUser(
                    LoginRequest(
                        login,
                        password
                    ),
                    activityContext
                )
            }
        }
    }

    fun onRegisterClick() {
        navigateToRegisterViewModel.value = true
    }

}