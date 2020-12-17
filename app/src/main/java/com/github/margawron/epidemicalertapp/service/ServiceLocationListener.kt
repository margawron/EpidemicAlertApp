package com.github.margawron.epidemicalertapp.service

import android.app.Notification
import android.app.NotificationManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


class ServiceLocationListener constructor(
    private val measurementRepository: MeasurementRepository,
    private val notificationManager: NotificationManager,
    private val notificationBuilder: Notification.Builder,
    private val measurementRegisteredCallback: MeasurementRegisteredCallback
) : LocationListener {

    companion object{
        var lastLocation: Location? = null
    }

    var measurementsCount = 0

    override fun onLocationChanged(newLocation: Location?) {
        val lastLocationMeasurements = mutableListOf<Location>()
        measurementsCount++
        if(newLocation == null || newLocation.accuracy > 21) return
        lastLocationMeasurements.add(newLocation)
        val size = lastLocationMeasurements.size
        if(measurementsCount > 10 || size > 0){
            measurementRegisteredCallback.onRegisteredMeasurement(this)
            val highestAccuracyLocation = lastLocationMeasurements.minByOrNull { location -> location.accuracy }!!

            if (lastLocation != null){
                val accRMS = sqrt(lastLocationMeasurements.sumOf { location -> location.accuracy.pow(2).toDouble() } / size)
                if(lastLocation!!.distanceTo(highestAccuracyLocation) > accRMS){
                    registerLocation(highestAccuracyLocation)
                    lastLocation = highestAccuracyLocation
                }
            }else{
                registerLocation(highestAccuracyLocation)
                lastLocation = highestAccuracyLocation
            }
        }
    }

    private fun registerLocation(location: Location) {
        CoroutineScope(Dispatchers.IO).launch {
            measurementRepository.addLocationForLoggedInUser(location)
        }
        val lat = if (location.latitude > 0) "N" else "S"
        val long = if (location.latitude > 0) "E" else "W"
        val time = LocalDateTime.ofInstant(Instant.ofEpochMilli(location.time), ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        notificationBuilder.setContentText(
            "$time ${abs(location.latitude)}°$lat ${location.longitude}°$long"
        )
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
