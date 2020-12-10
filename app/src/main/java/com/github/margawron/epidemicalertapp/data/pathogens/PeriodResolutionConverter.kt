package com.github.margawron.epidemicalertapp.data.pathogens

import androidx.room.TypeConverter
import java.lang.IllegalStateException

class PeriodResolutionConverter {

    @TypeConverter
    fun fromEnum(periodResolution: PeriodResolution): String {
        return when(periodResolution){
            PeriodResolution.MINUTES -> "M"
            PeriodResolution.HOURS -> "H"
            PeriodResolution.DAYS -> "D"
        }
    }

    @TypeConverter
    fun fromString(string: String): PeriodResolution{
        return when(string) {
            "M" -> PeriodResolution.MINUTES
            "H" -> PeriodResolution.HOURS
            "D" -> PeriodResolution.DAYS
            else -> throw IllegalStateException("Invalid period resolution mapping string")
        }
    }
}