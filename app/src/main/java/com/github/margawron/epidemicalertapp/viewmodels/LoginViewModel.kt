package com.github.margawron.epidemicalertapp.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.auth.AuthService
import com.github.margawron.epidemicalertapp.auth.LoginRequest
import com.github.margawron.epidemicalertapp.util.PreferenceHelper
import dagger.hilt.android.qualifiers.ApplicationContext


class LoginViewModel @ViewModelInject internal constructor(
    preferenceHelper: PreferenceHelper,
    private val authService: AuthService,
    @ApplicationContext private val context: Context,
    ) : ViewModel(){

    var login = preferenceHelper.getLastLoggedUsername()
    var password = preferenceHelper.getLastLoggedPassword()
    var rememberPassword = preferenceHelper.getShouldRememberPassword()

    fun onLoginClick(){
        authService.getBearerToken(LoginRequest(
            login,
            password
        )).subscribe(
        )
    }

    fun onRegisterClick(){
        Log.d("OE", "TEST")
        Toast.makeText(context, "Register binding works", Toast.LENGTH_SHORT).show()
    }

}