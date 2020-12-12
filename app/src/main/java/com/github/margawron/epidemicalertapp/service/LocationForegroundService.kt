package com.github.margawron.epidemicalertapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.github.margawron.epidemicalertapp.R
import kotlinx.coroutines.*

class LocationForegroundService : Service() {

    companion object{
        const val CHANNEL_ID = "location"
    }

    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel(CHANNEL_ID, getString(R.string.location_history_gathering_service), NotificationManager.IMPORTANCE_HIGH)
        getSystemService(NotificationManager::class.java)!!.createNotificationChannel(notificationChannel)
        val builder =
            Notification.Builder(applicationContext, CHANNEL_ID)
        with(builder) {
            setContentTitle(getString(R.string.location_history_gathering_service))
            setSmallIcon(R.drawable.ic_service_location)
        }
        val locationServiceNotification = builder.build()
        startForeground(1, locationServiceNotification)
        CoroutineScope(Dispatchers.IO).launch {

        }

    }

    override fun onBind(intent: Intent?): IBinder? = null
}