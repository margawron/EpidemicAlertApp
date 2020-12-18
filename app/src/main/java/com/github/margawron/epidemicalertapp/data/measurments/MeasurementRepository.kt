package com.github.margawron.epidemicalertapp.data.measurments

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.margawron.epidemicalertapp.api.measurements.MeasurementService
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.data.users.User
import java.lang.IllegalStateException
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
    private val cachedLastLocation: MutableLiveData<Location> by lazy {
        MutableLiveData<Location>()
    }

    fun getCurrentLocation(): LiveData<Location> {
        return cachedLastLocation
    }

    suspend fun registerLocation(location: Location){
        val user = authManager.getLoggedInUser()
        cachedLastLocation.postValue(location)
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
}