package com.github.margawron.epidemicalertapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.margawron.epidemicalertapp.databinding.MarkPlaceActivityBinding

class MarkPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: MarkPlaceActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.mark_place_activity)
    }
}