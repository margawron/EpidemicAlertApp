package com.github.margawron.epidemicalertapp.auth

import com.github.margawron.epidemicalertapp.api.*
import com.github.margawron.epidemicalertapp.api.auth.AuthService
import com.github.margawron.epidemicalertapp.api.auth.messages.LoginRequest
import com.github.margawron.epidemicalertapp.api.auth.messages.LoginResponse
import com.github.margawron.epidemicalertapp.api.auth.messages.RegisterRequest
import com.github.margawron.epidemicalertapp.api.auth.messages.RegisterResponse
import com.github.margawron.epidemicalertapp.data.users.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import java.time.Instant

class AuthManager(private val authService: AuthService) {

    private var token: String? = null
    private var loggedInUser: User? = null

    private var tokenExpiryInstant: Instant? = null

    private var loginRequest: LoginRequest? = null

    suspend fun registerUser(registerRequest: RegisterRequest): ApiResponse<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            authService.registerUser(registerRequest)
        }
    }

    suspend fun loginUser(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        val tokenResponse = authService.getBearerToken(loginRequest)
        if (tokenResponse is ApiResponse.Success) {
            val (accessToken, expirationDate) = tokenResponse.body!!
            token = accessToken
            tokenExpiryInstant = expirationDate
            this.loginRequest = loginRequest

        }
        return tokenResponse
    }

    suspend fun withValidToken(callback: () -> Any) {
        val expiryInstant = tokenExpiryInstant ?: throw IllegalStateException("Auth manager was not correctly initialized")
        if(expiryInstant.isAfter(Instant.now().minusSeconds(60))){
            val capturedRequest = loginRequest
            if(capturedRequest != null){
                loginUser(capturedRequest)
            }
        }
        callback()
    }

    fun getToken(): String? = token

    fun getLoggedInUser(): User? = loggedInUser
}