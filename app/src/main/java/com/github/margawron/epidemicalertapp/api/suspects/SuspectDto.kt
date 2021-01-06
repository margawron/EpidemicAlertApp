package com.github.margawron.epidemicalertapp.api.suspects

import com.github.margawron.epidemicalertapp.data.alerts.SuspicionLevel
import java.time.Instant

data class SuspectDto(
    var id: Long?,
    var startTime: Instant,
    var suspicionLevel: SuspicionLevel,
    var suspectId: Long,
    var pathogenId: Long
)