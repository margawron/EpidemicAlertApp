package com.github.margawron.epidemicalertapp.service

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import javax.inject.Inject


class ServiceLocationListener @Inject constructor(
    private val measurementRepository: MeasurementRepository
) : LocationListener {
    override fun onLocationChanged(location: Location?) {
        measurementRepository.addLocationForLoggedInUser(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d("OE", "Provider status changed $provider $status")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d("OE", "Provider enabled $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d("OE", "Provider disabled $provider")
    }
}
