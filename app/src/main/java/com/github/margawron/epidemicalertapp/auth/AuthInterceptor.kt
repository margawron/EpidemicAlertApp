package com.github.margawron.epidemicalertapp.auth

import android.util.Log
import androidx.lifecycle.LiveData
import com.github.margawron.epidemicalertapp.data.users.User
import com.github.margawron.epidemicalertapp.data.users.UserDao
import com.github.margawron.epidemicalertapp.util.PreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val userDao: UserDao,
    private val preferenceHelper: PreferenceHelper
) : Interceptor {
    private var loggedInUser: LiveData<User>? = null

    fun setLoggedInUser(user: LiveData<User>) {
        loggedInUser = user
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request();
        val token = loggedInUser?.value?.token
        val requestBuilder = request.newBuilder()
        if(token != null) requestBuilder.addHeader("Authorization", token)
        Log.d("test", request.url().toString())
        return chain.proceed(requestBuilder.build())
    }

}