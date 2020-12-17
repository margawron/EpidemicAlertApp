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
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(
            notificationChannel
        )
        val builder =
            Notification.Builder(applicationContext, CHANNEL_ID)
        with(builder) {
            setContentTitle(getString(R.string.location_history_gathering_service))
            setSmallIcon(R.drawable.ic_service_location)
            setOnlyAlertOnce(true)
        }
        val locationServiceNotification = builder.build()
        startForeground(1, locationServiceNotification)

        createNotificationDescriptionRefreshingLocationListener(notificationManager, builder)
    }

    private fun createNotificationDescriptionRefreshingLocationListener(
        notificationManager: NotificationManager,
        builder: Notification.Builder
    ) {
        val locationManager = getSystemService(LocationManager::class.java)
        val permissionManager = PermissionManager.getInstance(this)

        val serviceLocationListener =
            ServiceLocationListener(measurementRepository, notificationManager, builder) {
                setupNextMeasurementLocationListener(locationManager!!, it, permissionManager)
            }

        checkPermissions(permissionManager, locationManager, serviceLocationListener)
    }

    private fun setupNextMeasurementLocationListener(
        locationManager: LocationManager,
        it: ServiceLocationListener,
        permissionManager: PermissionManager
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            locationManager.removeUpdates(it)
            delay(20_000)
            checkPermissions(permissionManager, locationManager, it)
        }
    }

    private fun checkPermissions(
        permissionManager: PermissionManager,
        locationManager: LocationManager,
        serviceLocationListener: ServiceLocationListener
    ) {
        permissionManager.checkPermissions(
            Permissions.getNecessaryLocationPermissions(),
            object : PermissionManager.PermissionRequestListener {

                @SuppressLint("MissingPermission")
                override fun onPermissionGranted() {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1_000,
                        1.0f,
                        serviceLocationListener
                    )
                }

                override fun onPermissionDenied(deniedPermissions: DeniedPermissions) {
                    stopSelf()
                }
            })
    }

}


