package com.github.margawron.epidemicalertapp.viewmodels.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.LocationDisplayActivity
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.RegisterActivity
import com.github.margawron.epidemicalertapp.api.auth.LoginRequest
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.service.LocationForegroundService
import com.github.margawron.epidemicalertapp.util.PreferenceHelper
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel @ViewModelInject internal constructor(
    private val preferenceHelper: PreferenceHelper,
    private val authManager: AuthManager,
    @ActivityContext private val context: Context,
) : ViewModel() {
    var login = preferenceHelper.getLastLoggedUsername()
    var password = preferenceHelper.getLastLoggedPassword()
    var rememberPassword = preferenceHelper.getShouldRememberPassword()

    init {
        if (preferenceHelper.getShouldRememberPassword()) {
            onLoginClick()
        }
    }


    fun onLoginClick() {
        login()
    }

    private fun login() {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener {
                viewModelScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        return@withContext authManager.loginUser(
                            LoginRequest(
                                login,
                                password,
                                it,
                                Build.MANUFACTURER,
                                Build.DEVICE,
                                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                            )
                        )
                    }
                    when (response) {
                        is ApiResponse.Success -> {
                            checkIfShouldRememberPassword()
                            val locationManager =
                                context.getSystemService(LocationManager::class.java)
                            val gpsEnabled =
                                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                            if (!gpsEnabled) {
                                with(AlertDialog.Builder(context)) {
                                    setMessage(context.getString(R.string.app_requires_gps))
                                    setNeutralButton(android.R.string.ok) { dialog, _ ->
                                        dialog.dismiss()
                                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                    }.create()
                                        .show()
                                }
                            } else {
                                val foregroundIntent =
                                    Intent(context, LocationForegroundService::class.java)
                                context.startForegroundService(foregroundIntent)
                                val locationDisplayIntent =
                                    Intent(context, LocationDisplayActivity::class.java)
                                locationDisplayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                context.startActivity(locationDisplayIntent)
                            }
                        }
                        is ApiResponse.Error -> {
                            val errorBuilder = ApiResponse.errorToMessage(response)
                            Toast.makeText(context, errorBuilder, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    context.getString(R.string.device_could_not_be_registered),
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("OE", it.message ?: "Unknown error")
            }
    }

    private fun checkIfShouldRememberPassword() {
        if (rememberPassword) {
            preferenceHelper.setLastLoggedUsername(login)
            preferenceHelper.setLastLoggedPassword(password)
            preferenceHelper.setShouldRememberPassword(true)
        } else {
            preferenceHelper.setLastLoggedUsername("")
            preferenceHelper.setLastLoggedPassword("")
            preferenceHelper.setShouldRememberPassword(false)
        }
    }

    fun onRegisterClick() {
        context.startActivity(Intent(context, RegisterActivity::class.java))
    }

}