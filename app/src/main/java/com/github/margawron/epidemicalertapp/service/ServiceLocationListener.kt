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
import java.time.LocalDateTime


class ServiceLocationListener constructor(
    private val measurementRepository: MeasurementRepository,
    private val notificationManager: NotificationManager,
    private val notificationBuilder: Notification.Builder,
    private val accurateMeasurementCallback: AccurateMeasurementCallback
) : LocationListener {

    companion object{
        var lastLocation: Location? = null
    }

    override fun onLocationChanged(location: Location?) {
        // TODO change to lower number

        if(location == null || location.accuracy > 21) return
        else accurateMeasurementCallback.onAccurateMeasurement(this)
        if(lastLocation != null){
            if(lastLocation?.distanceTo(location)!! > lastLocation?.accuracy!!){
                CoroutineScope(Dispatchers.IO).launch {
                    measurementRepository.addLocationForLoggedInUser(location)
                }
                notificationBuilder.setContentText("${LocalDateTime.now().toLocalTime()} Szer:${location.latitude} Dług:${location.longitude}")
                notificationManager.notify(1, notificationBuilder.build())
            }
        }
        lastLocation = location
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
