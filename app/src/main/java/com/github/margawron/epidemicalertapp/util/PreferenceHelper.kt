package com.github.margawron.epidemicalertapp.util

import android.content.Context
import com.github.margawron.epidemicalertapp.R

class PreferenceHelper(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

    fun setLastLoggedUsername(username: String): Boolean = sharedPreferences.edit().putString(context.getString(R.string.preference_last_logged_username), username).commit()

    fun getLastLoggedUsername(): String? =  sharedPreferences.getString(context.getString(R.string.preference_last_logged_username), null)

}