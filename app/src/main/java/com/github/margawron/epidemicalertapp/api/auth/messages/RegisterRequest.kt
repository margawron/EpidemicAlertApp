package com.github.margawron.epidemicalertapp.api.auth.messages

data class RegisterRequest(val login: String, val password: String, val email: String)