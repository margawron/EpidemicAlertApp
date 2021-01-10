package com.github.margawron.epidemicalertapp.service

import android.app.Notification
import android.app.NotificationManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.github.margawron.epidemicalertapp.util.LocationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

class ServiceLocationListener constructor(
    private val measurementRepository: MeasurementRepository,
    private val notificationManager: NotificationManager,
    private val notificationBuilder: Notification.Builder,
    private val measurementRegisteredCallback: MeasurementRegisteredCallback
) : LocationListener {

    var measurementsCount = 0
    private val lastLocationMeasurements = mutableListOf<Location>()

    override fun onLocationChanged(newLocation: Location?) {
        measurementsCount++
        if(newLocation == null || newLocation.accuracy > 21) return
        lastLocationMeasurements.add(newLocation)
        val size = lastLocationMeasurements.size
        if(measurementsCount > 10 || size > 0){
            measurementRegisteredCallback.onRegisteredMeasurement(this)
            val highestAccuracyLocation = lastLocationMeasurements.minByOrNull { location -> location.accuracy }!!
            val lastLocation = measurementRepository.getCurrentLocation()
            if (lastLocation != null){
                val dateOfLastUpdate = Instant.ofEpochMilli(lastLocation.time).atZone(ZoneId.systemDefault()).toLocalDate()
                val todayDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
                val lastUpdateIsOld = todayDate.isAfter(dateOfLastUpdate.plusDays(1))
                val hasUserMovedSufficiently = lastLocation.distanceTo(highestAccuracyLocation) > lastLocation.accuracy + highestAccuracyLocation.accuracy
                if(hasUserMovedSufficiently || lastUpdateIsOld ){
                    onLocationApproved(highestAccuracyLocation)
                }
                lastLocationMeasurements.clear()
            }else{
                onLocationApproved(highestAccuracyLocation)
            }
        }
    }

    private fun onLocationApproved(location: Location) {
        CoroutineScope(Dispatchers.IO).launch {
            measurementRepository.registerLocation(location)
        }
        val time = LocationUtil.getFormattedLocation(location)
        notificationBuilder.setContentText(time)
        notificationManager.notify(1, notificationBuilder.build())
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

    fun interface MeasurementRegisteredCallback {
        fun onRegisteredMeasurement(it: ServiceLocationListener)
    }
}
