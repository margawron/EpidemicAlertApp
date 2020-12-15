package com.github.margawron.epidemicalertapp.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    fun addTimestampIndexToMeasurements(): Migration =
        object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE INDEX MeasurementTakenTimeIndex ON Measurement(measurement_time)")
            }
        }
}