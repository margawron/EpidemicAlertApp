package com.github.margawron.epidemicalertapp.data.poi

import androidx.room.TypeConverter
import java.lang.IllegalStateException

class PoiTypeConverter {

    @TypeConverter
    fun fromEnum(poiType: PoiType): String {
        return when(poiType) {
            PoiType.INFO -> "I"
            PoiType.QUARANTINE -> "Q"
        }
    }

    @TypeConverter
    fun fromString(string: String): PoiType {
        return when(string) {
            "I" -> PoiType.INFO
            "Q" -> PoiType.QUARANTINE
            else -> throw IllegalStateException("Invalid point of interest type mapping string")
        }
    }

}