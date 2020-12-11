package com.github.margawron.epidemicalertapp.auth

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.margawron.epidemicalertapp.api.ApiResponse
import com.github.margawron.epidemicalertapp.data.users.AccountState
import com.github.margawron.epidemicalertapp.data.users.Role
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.LocalDateTime
import java.util.*

data class LoginRequest(val login: String, val password: String)
data class RegisterRequest(val login: String, val password: String, val email: String)

data class LoginResponse(
    @JsonProperty(value = "access_token")
    val accessToken: String,
    @JsonProperty(value = "expiration_date")
    val expirationDate: Date,
    @JsonProperty(value = "token_type")
    val tokenType: String
)

data class RegisterResponse(
    var id: Long?,
    var username: String,
    var useremail: String,
    var role: Role,
    var accountCreationDate: LocalDateTime,
    var accountExpirationDate: LocalDateTime?,
    var accountState: AccountState
)


interface AuthService {
    @POST("register/")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): ApiResponse<RegisterResponse>

    @POST("auth/")
    suspend fun getBearerToken(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>
}

