package com.github.margawron.epidemicalertapp.service

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ServiceLocationListener constructor(
    private val measurementRepository: MeasurementRepository,
    private val accurateMeasurementCallback: AccurateMeasurementCallback
) : LocationListener {
    override fun onLocationChanged(location: Location?) {
        // TODO change to lower nubmer
        if (location == null || location.accuracy < 21) return
        else accurateMeasurementCallback.onAccurateMeasurement(this)

        // TODO znalezc najbardziej dokładny pomiar
        CoroutineScope(Dispatchers.IO).launch {
            measurementRepository.addLocationForLoggedInUser(location)
        }
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

    fun interface AccurateMeasurementCallback {
        fun onAccurateMeasurement(it: ServiceLocationListener)
    }
}
