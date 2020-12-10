package com.github.margawron.epidemicalertapp.auth

import com.github.margawron.epidemicalertapp.api.ApiResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.LocalDateTime

data class LoginRequest(val login: String, val password: String)
data class RegisterRequest(val login: String, val password: String, val email: String)

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String)

data class RegisterResponse(
    var id: Long?,
    var username: String,
    var useremail: String,
    var role: Role,
    var accountCreationDate: LocalDateTime,
    var accountExpirationDate: LocalDateTime?,
    var accountState: AccountState
)

enum class Role {
    USER,
    MODERATOR,
    ADMINISTRATOR;
}

enum class AccountState {
    NORMAL,
    BANNED;
}

interface AuthService {
    @POST("register/")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): ApiResponse<RegisterResponse>

    @POST("auth/")
    suspend fun getBearerToken(@Body loginRequest: LoginRequest) : ApiResponse<LoginResponse>
}

