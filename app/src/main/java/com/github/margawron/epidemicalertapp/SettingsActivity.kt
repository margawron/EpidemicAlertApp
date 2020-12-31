package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: SettingsActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.settings_activity)

    }
}