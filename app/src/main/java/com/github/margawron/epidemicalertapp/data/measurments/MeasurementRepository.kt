package com.github.margawron.epidemicalertapp.data.measurments

import android.location.Location
import androidx.lifecycle.LiveData
import com.github.margawron.epidemicalertapp.api.measurements.MeasurementService
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.data.users.User
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class MeasurementRepository @Inject constructor(
    private val measurementDao: MeasurementDao,
    private val measurementService: MeasurementService,
    private val authManager: AuthManager
) {
    private var sentCounter = 0

    private var cachedLastLocation: Location? = null

    fun getCurrentLocation(): Location? = cachedLastLocation

    suspend fun registerLocation(location: Location){
        val user = authManager.getLoggedInUser()
        cachedLastLocation = location
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

    fun getLocationsForDay(user: User, date: LocalDate): LiveData<List<Measurement>>{
        val startOfDay = date.atStartOfDay(ZoneId.systemDefault());
        val endOfDay = startOfDay.plus(1, ChronoUnit.DAYS)
        return measurementDao.getMeasurementsFromDate(user.id, startOfDay.toInstant(), endOfDay.toInstant())
    }

    fun getLastLocation(): LiveData<Measurement?> {
        val user = authManager.getLoggedInUser()
        return measurementDao.getLastLocationForUser(user.id)
    }
}