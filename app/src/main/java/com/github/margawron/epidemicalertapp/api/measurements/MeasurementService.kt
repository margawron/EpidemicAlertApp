package com.github.margawron.epidemicalertapp.api.measurements

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import retrofit2.http.POST

interface MeasurementService {
    @POST
    suspend fun insertMeasurements(measurements: List<Measurement>): ApiResponse<Nothing>
}