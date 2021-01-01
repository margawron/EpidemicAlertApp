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
import kotlin.collections.set

class LocationRepository @Inject constructor(
    private val locationService: LocationService,
    private val measurementRepository: MeasurementRepository,
) {

    private val locations = mutableMapOf<Long, LocationDto>()
    fun getLocations(): Collection<LocationDto> = locations.values

    private val mutableLiveData = MutableLiveData<Collection<LocationDto>>()
    fun getLocationsLiveData(): LiveData<Collection<LocationDto>> = mutableLiveData

    private fun addLocations(vararg locations: LocationDto) {
        locations.forEach {
            this.locations[it.id!!] = it
        }
        mutableLiveData.postValue(this.locations.values)
    }

    suspend fun fetchLocationNearby(context: Context, lifecycleOwner: LifecycleOwner) {
        val data = measurementRepository.getLastLocation()
        val observer = Observer<Measurement?> {
            CoroutineScope(Dispatchers.IO).launch {
                if (it != null) {
                    when (val response =
                        locationService.getNearbyLocations(it.latitude, it.longitude)) {
                        is ApiResponse.Success -> {
                            addLocations(*(response.body!!.toTypedArray()))
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
                addLocations(it)
            }
        }
        return apiResponse
    }
}