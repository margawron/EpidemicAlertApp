package com.github.margawron.epidemicalertapp.api.users

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import retrofit2.http.GET

interface UserService {
    @GET("users/self")
    suspend fun getSelf(): ApiResponse<UserDto>
}