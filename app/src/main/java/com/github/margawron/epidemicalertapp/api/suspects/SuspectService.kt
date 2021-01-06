package com.github.margawron.epidemicalertapp.api.suspects

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface SuspectService {

    @POST("suspect")
    suspend fun reportSuspect(@Body suspectDto: SuspectDto): ApiResponse<Any>

}