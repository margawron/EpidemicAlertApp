package com.github.margawron.epidemicalertapp.data.alerts

import com.github.margawron.epidemicalertapp.R

enum class SuspicionLevel(val stringId: Int){
    SICK(R.string.suspicion_level_sick),
    PROBABLE(R.string.suspicion_level_probable),
    DISMISSED(R.string.suspicion_level_dismissed)
}