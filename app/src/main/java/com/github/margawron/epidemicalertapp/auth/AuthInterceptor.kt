package com.github.margawron.epidemicalertapp.auth

import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val lazyAuthManager: Lazy<AuthManager>,
) : Interceptor {

    var isDuringTokenRefresh: AtomicBoolean = AtomicBoolean(false)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        val authManager = lazyAuthManager.get()

        val token = authManager.getToken()
        if (token != null) {
            if(!authManager.isTokenValid() && !isDuringTokenRefresh.compareAndSet(false, true)){
                runBlocking {
                    authManager.refreshToken()
                }
                isDuringTokenRefresh.set(false)
            }
            requestBuilder.addHeader("Authorization", token)
        }
        return chain.proceed(requestBuilder.build())
    }

}