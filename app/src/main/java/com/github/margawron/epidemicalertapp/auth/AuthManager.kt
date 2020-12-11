package com.github.margawron.epidemicalertapp.auth

import android.content.Context
import com.github.margawron.epidemicalertapp.api.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import java.time.Instant
import java.util.*

class AuthManager(private val authService: AuthService) {

    private var token: String? = null
    private var tokenExpiryDate: Date? = null

    private var loginRequest: LoginRequest? = null

    suspend fun registerUser(registerRequest: RegisterRequest, activityContext: Context): Boolean {
        val response = withContext(Dispatchers.IO) {
            authService.registerUser(registerRequest)
        }
        response

        return false
    }

    suspend fun loginUser(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        val tokenResponse = authService.getBearerToken(loginRequest)
        if (tokenResponse is ApiResponse.SuccessWithBody) {
            val (accessToken, expirationDate) = tokenResponse.body
            token = accessToken
            tokenExpiryDate = expirationDate
            this.loginRequest = loginRequest
        }
        return tokenResponse
    }

    suspend fun withValidToken(callback: () -> Any) {
        val expiryDate = tokenExpiryDate ?: throw IllegalStateException("Auth manager was not correctly initialized")
        if(expiryDate.after(Date.from(Instant.now().minusSeconds(60)))){
            val capturedRequest = loginRequest
            if(capturedRequest != null){
                loginUser(capturedRequest)
            }
        }
        callback()
    }

    fun getToken(): String? {
        return token
    }
}