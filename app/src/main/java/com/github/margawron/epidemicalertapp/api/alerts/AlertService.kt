package com.github.margawron.epidemicalertapp.api.alerts

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AlertService {

    @GET("alert/{id}")
    suspend fun getSingleAlert(@Path("id") alertId: Long) : ApiResponse<AlertDto>

    @GET("alert/all")
    suspend fun getAllOwnAlerts(): ApiResponse<List<AlertDto>>

}