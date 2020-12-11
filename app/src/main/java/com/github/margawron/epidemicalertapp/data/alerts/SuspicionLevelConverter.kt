package com.github.margawron.epidemicalertapp.data.alerts

import androidx.room.TypeConverter
import java.lang.IllegalStateException

class SuspicionLevelConverter {

    @TypeConverter
    fun fromEnum(suspicionLevel: SuspicionLevel): String{
        return when(suspicionLevel){
            SuspicionLevel.SICK -> "S"
            SuspicionLevel.PROBABLE -> "P"
            SuspicionLevel.DISMISSED -> "D"
        }
    }

    @TypeConverter
    fun fromString(string: String): SuspicionLevel {
        return when(string){
            "S" -> SuspicionLevel.SICK
            "P" -> SuspicionLevel.PROBABLE
            "D" -> SuspicionLevel.DISMISSED
            else -> throw IllegalStateException("Invalid suspicion level mapping string")
        }
    }
}