package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.databinding.ChangePriviliagesActivityBinding

class ChangePrivilegesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ChangePriviliagesActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.change_priviliages_activity)
    }
}