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
            val lastLocation = measurementRepository.getLastLocation().value
            if (lastLocation != null){
                if(lastLocation.distanceTo(highestAccuracyLocation) > lastLocation.accuracy + highestAccuracyLocation.accuracy){
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
