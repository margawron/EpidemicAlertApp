package com.github.margawron.epidemicalertapp.api.measurements

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.api.common.IdMapping
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import retrofit2.http.POST
import retrofit2.http.Query

interface MeasurementService {
    @POST("measurement")
    suspend fun insertMeasurements(@Query("deviceId") deviceId: Long, measurements: List<MeasurementDto>): ApiResponse<List<IdMapping>>
}