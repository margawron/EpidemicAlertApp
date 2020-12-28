package com.github.margawron.epidemicalertapp.api.auth

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class LoginResponse(
    @JsonProperty(value = "access_token")
    val accessToken: String,
    @JsonProperty(value = "expiration_date")
    val expirationDate: Instant,
    @JsonProperty(value = "token_type")
    val tokenType: String,
    @JsonProperty(value = "device_id")
    val deviceId: Long,
)