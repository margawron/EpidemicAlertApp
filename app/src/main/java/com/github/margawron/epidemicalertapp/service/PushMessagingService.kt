package com.github.margawron.epidemicalertapp.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class PushMessagingService @Inject constructor(

): FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notification = message.notification
        val body = message.data
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}