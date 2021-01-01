package com.github.margawron.epidemicalertapp.data.locations

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.api.location.LocationDto
import com.github.margawron.epidemicalertapp.api.location.LocationService
import com.github.margawron.epidemicalertapp.api.location.LocationType
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.set

@Singleton
class LocationRepository @Inject constructor(
    private val locationService: LocationService,
    private val measurementRepository: MeasurementRepository,
) {

    private var locations = mutableMapOf<Long, LocationDto>()
    fun getLocations(): Collection<LocationDto> = locations.values

    private val mutableLiveData = MutableLiveData<Collection<LocationDto>>()
    fun getLocationsLiveData(): LiveData<Collection<LocationDto>> = mutableLiveData

    private fun addLocations(incomingLocations: Collection<LocationDto>) {
        incomingLocations.forEach {
            this.locations[it.id!!] = it
        }
        mutableLiveData.postValue(locations.values)
    }

    private fun refreshLocations(incomingLocations: Collection<LocationDto>){
        val pairs = incomingLocations.map { it.id!! to it }.toTypedArray()
        locations = mutableMapOf(*(pairs))
        mutableLiveData.postValue(locations.values)
    }

    private fun removeLocation(locationId: Long){
        locations.remove(locationId)
        mutableLiveData.postValue(locations.values)
    }

    suspend fun removeLocationById(locationId: Long): ApiResponse<Any> {
        val response = locationService.deleteLocation(locationId)
        when(response) {
            is ApiResponse.Success -> {
                removeLocation(locationId)
            }
            else -> {}
        }
        return response
    }

    suspend fun fetchLocationNearby(context: Context, lifecycleOwner: LifecycleOwner) {
        val data = measurementRepository.getLastLocation()
        val observer = Observer<Measurement?> {
            CoroutineScope(Dispatchers.IO).launch {
                if (it != null) {
                    when (val response =
                        locationService.getNearbyLocations(it.latitude, it.longitude)) {
                        is ApiResponse.Success -> {
                            refreshLocations(response.body!!)
                        }
                        is ApiResponse.Error -> {
                            val error = ApiResponse.errorToMessage(response)
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    }
                    withContext(Dispatchers.Main){
                        data.removeObservers(lifecycleOwner)
                    }
                }
            }
        }
        withContext(Dispatchers.Main) {
            data.observe(lifecycleOwner, observer)
        }
    }

    suspend fun createLocation(
        latLng: LatLng,
        description: String,
        expiryTime: Instant,
        locationType: LocationType
    ): ApiResponse<LocationDto> {
        val locationDto = LocationDto(
            null,
            expiryTime,
            latLng.latitude,
            latLng.longitude,
            locationType,
            description
        )
        val apiResponse = locationService.createNewLocation(locationDto)
        if (apiResponse is ApiResponse.Success) {
            apiResponse.body?.let {
                addLocations(listOf(it))
            }
        }
        return apiResponse
    }
}