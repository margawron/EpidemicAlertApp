package com.github.margawron.epidemicalertapp.api.zones

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ZoneService {

    @GET("zone/{name}")
    suspend fun getZoneByName(@Path("name") zoneName: String): ApiResponse<ZoneDto>

    @GET("zone/")
    suspend fun getAllZones(): ApiResponse<List<ZoneDto>>

    @POST("zone/modify")
    suspend fun modifyZone(@Body zoneDto: ZoneDto): ApiResponse<ZoneDto>

    @POST("zone/modify")
    suspend fun modifyMultipleZones(@Body zones: List<ZoneDto>): ApiResponse<List<ZoneDto>>

}