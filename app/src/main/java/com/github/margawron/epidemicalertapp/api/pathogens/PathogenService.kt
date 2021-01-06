package com.github.margawron.epidemicalertapp.api.pathogens

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.data.pathogens.Pathogen
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PathogenService {

    @GET("pathogen/id/{pathogenId}")
    suspend fun getPathogenById(@Path("pathogenId") pathogenId: Long): ApiResponse<Pathogen>

    @GET("pathogen/all")
    suspend fun getAllPathogens(): ApiResponse<List<Pathogen>>

    @GET("pathogen/name/{name}")
    suspend fun findPathogenByName(@Path("name") pathogenName: String): ApiResponse<List<Pathogen>>

    @POST("pathogen")
    suspend fun createPathogen(@Body pathogen: Pathogen): ApiResponse<Pathogen>

}