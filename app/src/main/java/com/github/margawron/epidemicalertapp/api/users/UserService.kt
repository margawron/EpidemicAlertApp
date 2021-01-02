package com.github.margawron.epidemicalertapp.api.users

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.data.users.Role
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET("users/self")
    suspend fun getSelf(): ApiResponse<UserDto>

    @GET("users/name/{name}")
    suspend fun getUserWithNameLike(@Path("name") nameLike: String): ApiResponse<List<UserDto>>

    @POST("users/id/{id}")
    suspend fun changeUserRole(@Path("id") userId: Long, @Body role: Role): ApiResponse<UserDto>
}