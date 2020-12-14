package com.github.margawron.epidemicalertapp.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.IBinder
import com.github.margawron.epidemicalertapp.R
import com.intentfilter.androidpermissions.PermissionManager
import com.intentfilter.androidpermissions.models.DeniedPermissions
import dagger.hilt.android.AndroidEntryPoint
import java.util.Collections.singleton
import javax.inject.Inject

@AndroidEntryPoint
class LocationForegroundService : Service() {

    @Inject
    lateinit var serviceLocationListener: ServiceLocationListener

    companion object {
        const val CHANNEL_ID = "location"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.location_history_gathering_service),
            NotificationManager.IMPORTANCE_HIGH
        )
        getSystemService(NotificationManager::class.java)!!.createNotificationChannel(
            notificationChannel
        )
        val builder =
            Notification.Builder(applicationContext, CHANNEL_ID)
        with(builder) {
            setContentTitle(getString(R.string.location_history_gathering_service))
            setSmallIcon(R.drawable.ic_service_location)
        }
        val locationServiceNotification = builder.build()
        startForeground(1, locationServiceNotification)

        createLocationListener()
    }

    private fun createLocationListener() {
        val locationManager = getSystemService(LocationManager::class.java)
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        val permissionManager = PermissionManager.getInstance(this)
        permissionManager.checkPermissions(
            singleton(Manifest.permission.ACCESS_FINE_LOCATION),
            object : PermissionManager.PermissionRequestListener {

                @SuppressLint("MissingPermission")
                override fun onPermissionGranted() {
                    val locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        60_000,
                        10.0f,
                        serviceLocationListener
                    )
                }

                override fun onPermissionDenied(deniedPermissions: DeniedPermissions) {
                    for (permission in deniedPermissions) {
                        if (permission.shouldShowRationale()) {

                        }
                    }
                }

            })

    }
}


