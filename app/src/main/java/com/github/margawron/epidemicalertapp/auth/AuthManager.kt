package com.github.margawron.epidemicalertapp.auth

import android.content.Context
import com.github.margawron.epidemicalertapp.api.ApiResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthManager @Inject constructor(
    private val authService: AuthService,
    @ApplicationContext context: Context
) {
    suspend fun registerUser(registerRequest: RegisterRequest, activityContext: Context): Boolean{
        val response = withContext(Dispatchers.IO){
            authService.registerUser(registerRequest)
        }
        response

        return false
    }
    suspend fun loginUser(loginRequest: LoginRequest, activityContext: Context): Boolean{
        val tokenResponse = authService.getBearerToken(loginRequest)
        if(tokenResponse is ApiResponse.SuccessWithBody){

        }
        return false
    }
}