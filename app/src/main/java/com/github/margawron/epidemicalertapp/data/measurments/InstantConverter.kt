package com.github.margawron.epidemicalertapp.data.measurments

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {

    @TypeConverter
    fun instantToLong(instant: Instant) = instant.toEpochMilli()

    @TypeConverter
    fun longToInstant(milis: Long) = Instant.ofEpochMilli(milis)
}