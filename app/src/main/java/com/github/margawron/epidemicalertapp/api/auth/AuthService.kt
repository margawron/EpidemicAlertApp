package com.github.margawron.epidemicalertapp.api.auth

import com.github.margawron.epidemicalertapp.api.auth.messages.LoginRequest
import com.github.margawron.epidemicalertapp.api.auth.messages.LoginResponse
import com.github.margawron.epidemicalertapp.api.auth.messages.RegisterRequest
import com.github.margawron.epidemicalertapp.api.auth.messages.RegisterResponse
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("register/")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): ApiResponse<RegisterResponse>

    @POST("auth/")
    suspend fun getBearerToken(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>
}

