package com.github.margawron.epidemicalertapp.api.users

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("users/self")
    suspend fun getSelf(): ApiResponse<UserDto>

    @GET("users/name/{name}")
    suspend fun getUserWithNameLike(@Path("name") nameLike: String): ApiResponse<List<UserDto>>
}