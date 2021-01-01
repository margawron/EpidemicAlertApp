package com.github.margawron.epidemicalertapp.viewmodels.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.LocationDisplayActivity
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.service.LocationForegroundService
import com.github.margawron.epidemicalertapp.util.PreferenceHelper
import dagger.hilt.android.qualifiers.ActivityContext

class SettingsViewModel @ViewModelInject internal constructor(
    private val preferencesHelper: PreferenceHelper,
    private val authManager: AuthManager,
    @ActivityContext private val context: Context
): ViewModel()
{
    lateinit var activity: AppCompatActivity
    var loginText = "Zalogowano jako: " + authManager.getLoggedInUser().login
    var roleText = "Rola użytkownika to: " + authManager.getLoggedInUser().role

    fun forgetCredentials(){
        preferencesHelper.setLastLoggedUsername("")
        preferencesHelper.setLastLoggedPassword("")
        preferencesHelper.setShouldRememberPassword(false)
    }

    fun turnOffLocationService(){
        context.stopService(Intent(context, LocationForegroundService::class.java))
    }

    fun logout(){
        turnOffLocationService()
        forgetCredentials()
        authManager.logout()
        val locationDisplayIntent = Intent(context, LocationDisplayActivity::class.java)
        locationDisplayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activity.startActivity(locationDisplayIntent)
    }
}