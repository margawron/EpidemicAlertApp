package com.github.margawron.epidemicalertapp.api.zones

import com.github.margawron.epidemicalertapp.R

enum class ZoneEmergencyLevel(val stringId: Int) {
    GREEN(R.string.emergency_level_green),
    YELLOW(R.string.emergency_level_yellow),
    RED(R.string.emergency_level_red);
}