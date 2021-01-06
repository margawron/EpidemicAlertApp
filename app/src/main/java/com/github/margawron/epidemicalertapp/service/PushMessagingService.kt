package com.github.margawron.epidemicalertapp.service

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

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var deviceService: DeviceService

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val deviceId = authManager.getDeviceId()
        if(deviceId == null) return
        else {
            CoroutineScope(Dispatchers.IO).launch {
                deviceService.updateFirebaseToken(deviceId, token)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notification = message.notification
        val body = message.data
    }
}