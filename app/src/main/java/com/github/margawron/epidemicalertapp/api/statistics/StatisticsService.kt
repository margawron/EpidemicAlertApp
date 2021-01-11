package com.github.margawron.epidemicalertapp.api.statistics

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import retrofit2.http.GET

interface StatisticsService {

    @GET("statistics")
    suspend fun getStatisticsFromLastMonth(): ApiResponse<Map<String, StatisticsDto>>
}