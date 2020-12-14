package com.github.margawron.epidemicalertapp.data.measurments

import android.location.Location
import com.github.margawron.epidemicalertapp.api.measurements.MeasurementService
import com.github.margawron.epidemicalertapp.auth.AuthManager
import java.lang.IllegalStateException
import java.time.Instant
import javax.inject.Inject

class MeasurementRepository @Inject constructor(
    private val measurementDao: MeasurementDao,
    private val measurementService: MeasurementService,
    private val authManager: AuthManager
) {
    private var sentCounter = 0;

    fun addLocationForLoggedInUser(location: Location?){
        if (location == null) return
        val user = authManager.getLoggedInUser()
            ?: throw IllegalStateException("User should not be null at this point")
        measurementDao.insert(
            Measurement(
                null,
                null,
                user.id,
                Instant.ofEpochMilli(location.time),
                location.latitude,
                location.longitude,
                location.accuracy,
                location.bearing,
                location.bearingAccuracyDegrees,
                false
            )
        )
        sentCounter++
    }
}