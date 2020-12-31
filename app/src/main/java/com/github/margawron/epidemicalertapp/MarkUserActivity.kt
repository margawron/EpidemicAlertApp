package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

class MarkUserActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: MarkUserActivity =
            DataBindingUtil.setContentView(this, R.layout.mark_user_activity)
    }
}