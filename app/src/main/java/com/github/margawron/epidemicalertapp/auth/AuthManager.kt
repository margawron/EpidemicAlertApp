package com.github.margawron.epidemicalertapp.auth

import com.github.margawron.epidemicalertapp.api.auth.AuthService
import com.github.margawron.epidemicalertapp.api.auth.LoginRequest
import com.github.margawron.epidemicalertapp.api.auth.LoginResponse
import com.github.margawron.epidemicalertapp.api.auth.RegisterRequest
import com.github.margawron.epidemicalertapp.api.auth.RegisterResponse
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.data.users.User
import com.github.margawron.epidemicalertapp.data.users.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException
import java.time.Instant

class AuthManager(
    private val userRepository: UserRepository,
    private val authService: AuthService,
) {

    private var token: String? = null
    private var loggedInUser: User? = null

    private var tokenExpiryInstant: Instant? = null

    private var loginRequest: LoginRequest? = null
    private var loginResponse: LoginResponse? = null
    fun getDeviceId() = loginResponse?.deviceId

    suspend fun registerUser(registerRequest: RegisterRequest): ApiResponse<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            authService.registerUser(registerRequest)
        }
    }

    suspend fun loginUser(loginRequest: LoginRequest): ApiResponse<LoginResponse> {
        val tokenResponse = authService.getBearerToken(loginRequest)
        if (tokenResponse is ApiResponse.Success) {
            loginResponse = tokenResponse.body!!
            val (accessToken, expirationDate) = tokenResponse.body
            token = accessToken
            tokenExpiryInstant = expirationDate
            this.loginRequest = loginRequest
            when(val apiResponse = userRepository.getSelfData()){
                is ApiResponse.Success -> {
                    val userDto = apiResponse.body!!
                    loggedInUser = userRepository.updateFetchedUser(userDto)
                }
                is ApiResponse.Error -> {
                    return apiResponse
                }
            }

        }
        return tokenResponse
    }

    suspend fun withValidToken(callback: () -> Any) {
        val expiryInstant = tokenExpiryInstant
            ?: throw IllegalStateException("Auth manager was not correctly initialized")
        if (expiryInstant.isAfter(Instant.now().minusSeconds(60))) {
            val capturedRequest = loginRequest
            if (capturedRequest != null) {
                loginUser(capturedRequest)
            }
        }
        callback()
    }

    fun getToken(): String? = token

    fun getLoggedInUser(): User {
        return loggedInUser ?: throw IllegalStateException("User did not yet login")
    }

    fun isUserLoggedIn(): Boolean = loggedInUser != null

    fun logout() {
        loggedInUser = null
        token = null
        tokenExpiryInstant = null
        loginRequest = null
    }
}