package com.github.margawron.epidemicalertapp.data.measurments

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import java.time.Instant

@Dao
@TypeConverters(value = [
    InstantConverter::class
])
interface MeasurementDao {
    @Insert
    fun insert(measurement: Measurement)

    @Query("SELECT * FROM Measurement WHERE owner_id = :user_id and measurement_time > :before and measurement_time < :after ORDER BY measurement_time ASC")
    fun getMeasurementsFromDate(user_id: Long, before: Instant, after: Instant): LiveData<List<Measurement>>

    @Query("SELECT * FROM Measurement WHERE owner_id = :user_id ORDER BY measurement_time DESC LIMIT 1")
    fun getLastLocationForUser(user_id: Long): LiveData<Measurement?>
}