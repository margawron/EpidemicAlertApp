package com.github.margawron.epidemicalertapp.viewmodels

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.LocationDisplayActivity
import com.github.margawron.epidemicalertapp.RegisterActivity
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.api.auth.LoginRequest
import com.github.margawron.epidemicalertapp.service.LocationForegroundService
import com.github.margawron.epidemicalertapp.util.PreferenceHelper
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel @ViewModelInject internal constructor(
    private val preferenceHelper: PreferenceHelper,
    private val authManager: AuthManager,
    @ActivityContext private val activityContext: Context,
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
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                return@withContext authManager.loginUser(
                    LoginRequest(
                        login,
                        password
                    )
                )
            }
            when (response) {
                is ApiResponse.Success -> {
                    checkIfShouldRememberPassword()
                    val locationManager =
                        activityContext.getSystemService(LocationManager::class.java)
                    val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    if (!gpsEnabled) {
                        with(AlertDialog.Builder(activityContext)) {
                            setMessage("Aplikacja potrzebuje dostępu do systemu GPS w celu zbierania historii lokalizacji")
                            setNeutralButton(android.R.string.ok) { dialog, _ ->
                                dialog.dismiss()
                                activityContext.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            }.create()
                                .show()
                        }
                    } else {
                        val foregroundIntent =
                            Intent(activityContext, LocationForegroundService::class.java)
                        activityContext.startForegroundService(foregroundIntent)
                        val locationDisplayIntent =
                            Intent(activityContext, LocationDisplayActivity::class.java)
                        locationDisplayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        activityContext.startActivity(locationDisplayIntent)
                    }
                }
                is ApiResponse.Error -> {
                    val errorBuilder = StringBuilder()
                    response.errors.joinTo(errorBuilder, postfix = "\n") { e -> e.errorMessage }
                    Toast.makeText(activityContext, errorBuilder, Toast.LENGTH_SHORT).show()
                }
            }
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
        activityContext.startActivity(Intent(activityContext, RegisterActivity::class.java))
    }

}