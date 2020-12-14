package com.github.margawron.epidemicalertapp.api.users

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.api.users.messages.UserDto
import retrofit2.http.GET

interface UserService {
    @GET("users/self")
    suspend fun getSelfData(): ApiResponse<UserDto>
}