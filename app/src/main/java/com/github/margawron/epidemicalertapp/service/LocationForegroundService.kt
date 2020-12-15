package com.github.margawron.epidemicalertapp.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.IBinder
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import com.intentfilter.androidpermissions.PermissionManager
import com.intentfilter.androidpermissions.models.DeniedPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationForegroundService : Service() {

    @Inject
    lateinit var measurementRepository: MeasurementRepository

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
        val permissionManager = PermissionManager.getInstance(this)


        val serviceLocationListener = ServiceLocationListener(measurementRepository) {
            setupAccurateLocationListener(locationManager, it, permissionManager)
        }
        permissionManager.checkPermissions(
            Permissions.getNecessaryLocationPermissions(),
            object : PermissionManager.PermissionRequestListener {

                @SuppressLint("MissingPermission")
                override fun onPermissionGranted() {
                    val locationProvider =
                        locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1_000,
                        1.0f,
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

    private fun setupAccurateLocationListener(
        locationManager: LocationManager,
        it: ServiceLocationListener,
        permissionManager: PermissionManager
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            locationManager.removeUpdates(it)
            delay(50_000)
            permissionManager.checkPermissions(
                Permissions.getNecessaryLocationPermissions(),
                object : PermissionManager.PermissionRequestListener {

                    @SuppressLint("MissingPermission")
                    override fun onPermissionGranted() {
                        val locationProvider =
                            locationManager.getProvider(LocationManager.GPS_PROVIDER)
                        CoroutineScope(Dispatchers.Main).launch {
                            locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                1_000,
                                1.0f,
                                it
                            )
                        }
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

}


