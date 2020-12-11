package com.github.margawron.epidemicalertapp.auth

import dagger.Lazy
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authManager: Lazy<AuthManager>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        val token = authManager.get().getToken()
        if (token != null) requestBuilder.addHeader("Authorization", token)
        return chain.proceed(requestBuilder.build())
    }

}