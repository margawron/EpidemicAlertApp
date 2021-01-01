package com.github.margawron.epidemicalertapp.api.measurements

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.api.common.IdMapping
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface MeasurementService {
    @POST("measurement")
    suspend fun insertMeasurements(
        @Query("deviceId") deviceId: Long,
        @Body measurements: List<MeasurementDto>
    ): ApiResponse<List<IdMapping>>
}