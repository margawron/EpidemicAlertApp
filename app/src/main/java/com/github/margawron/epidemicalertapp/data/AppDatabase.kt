package com.github.margawron.epidemicalertapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.github.margawron.epidemicalertapp.data.alerts.AlertDao
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementDao
import com.github.margawron.epidemicalertapp.data.pathogens.PathogenDao
import com.github.margawron.epidemicalertapp.data.poi.PoiLocationDao
import com.github.margawron.epidemicalertapp.data.proximity.ProximityMeasurementDao
import com.github.margawron.epidemicalertapp.data.users.User
import com.github.margawron.epidemicalertapp.data.users.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun measurementDao(): MeasurementDao
    abstract fun pathogenDao(): PathogenDao
    abstract fun alertDao(): AlertDao
    abstract fun proximityMeasurementDao(): ProximityMeasurementDao
    abstract fun poiLocationDao(): PoiLocationDao

    companion object{
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase = Room
            .databaseBuilder(context, AppDatabase::class.java, "epidemic-alert-db")
            .build()
    }
}