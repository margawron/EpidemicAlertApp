package com.github.margawron.epidemicalertapp.viewmodels

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.qualifiers.ActivityContext

class SettingsViewModel @ViewModelInject internal constructor(
    @ActivityContext context: Context
): ViewModel()
{

    fun onSwitchRememberLogin(){

    }

    fun turnOffLocationService(){

    }

    fun logout(){

    }
}