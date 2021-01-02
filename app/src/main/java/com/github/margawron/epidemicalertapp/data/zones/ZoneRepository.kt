package com.github.margawron.epidemicalertapp.data.zones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.api.zones.ZoneDto
import com.github.margawron.epidemicalertapp.api.zones.ZoneService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ZoneRepository @Inject constructor(
    private val zoneService: ZoneService,
) {

    private lateinit var zonesInternal: List<ZoneDto>
    fun getZones() = zonesInternal

    private val zones: MutableLiveData<List<ZoneDto>> = MutableLiveData()
    fun getZonesLiveData(): LiveData<List<ZoneDto>> = zones

    private fun setZones(response: ApiResponse.Success<List<ZoneDto>>) {
        zonesInternal = response.body!!
        zones.postValue(zonesInternal)
    }

    suspend fun fetchZones(): ApiResponse<List<ZoneDto>>{
        val response = zoneService.getAllZones()
        when(response){
            is ApiResponse.Success -> {
                setZones(response)
            }
            else -> {}
        }
        return response
    }
}