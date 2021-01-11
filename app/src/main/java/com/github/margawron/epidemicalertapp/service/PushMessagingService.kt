package com.github.margawron.epidemicalertapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.margawron.epidemicalertapp.LocationDisplayActivity
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.api.devices.DeviceService
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PushMessagingService: FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "alert incoming"
    }

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var deviceService: DeviceService

    override fun onNewToken(token: String) {
        val deviceId = authManager.getDeviceId()
        if(deviceId == null) return
        else {
            CoroutineScope(Dispatchers.IO).launch {
                deviceService.updateFirebaseToken(deviceId, token)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val body = message.data
        val intent = Intent(this, LocationDisplayActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val alertId = body["id"]!!.toLong()
            putExtra("alertId", alertId)
        }

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.suspect_contact_title),
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(
            notificationChannel
        )
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_service_location)
            .setContentTitle(getString(R.string.suspect_contact_title))
            .setContentText(getString(R.string.suspect_contact_text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)){
            notify(2,builder.build())
        }
    }
}