package com.github.margawron.epidemicalertapp.api.location

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import retrofit2.http.*


interface LocationService {

    @GET("location")
    suspend fun getNearbyLocations(@Query("lat") latitude: Double, @Query("lng") longitude: Double): ApiResponse<List<LocationDto>>

    @POST("location")
    suspend fun createNewLocation(@Body locationDto: LocationDto): ApiResponse<LocationDto>

    @DELETE("location/{id}")
    suspend fun deleteLocation(@Path("id") locationId: Long): ApiResponse<Nothing>

}