package com.github.margawron.epidemicalertapp.api.devices

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DeviceService {

    @POST("device/{id}/fcmToken")
    suspend fun updateFirebaseToken(@Path("id") deviceId: Long, @Body firebaseToken: String): ApiResponse<DeviceDto>

}