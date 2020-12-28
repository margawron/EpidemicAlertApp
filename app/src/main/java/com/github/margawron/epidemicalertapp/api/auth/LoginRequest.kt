package com.github.margawron.epidemicalertapp.api.auth

data class LoginRequest(
    val login: String,
    val password: String,
    val fcmToken: String,
    val manufacturer: String,
    val deviceName: String,
    val serialNumber: String,
)