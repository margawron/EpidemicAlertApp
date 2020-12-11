package com.github.margawron.epidemicalertapp.di

import android.content.Context
import com.github.margawron.epidemicalertapp.data.AppDatabase
import com.github.margawron.epidemicalertapp.data.alerts.AlertDao
import com.github.margawron.epidemicalertapp.data.measurments.Measurement
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementDao
import com.github.margawron.epidemicalertapp.data.pathogens.Pathogen
import com.github.margawron.epidemicalertapp.data.pathogens.PathogenDao
import com.github.margawron.epidemicalertapp.data.users.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Singleton
    @Provides
    fun provideMeasurementDao(appDatabase: AppDatabase): MeasurementDao = appDatabase.measurementDao()

    @Singleton
    @Provides
    fun providePathogenDao(appDatabase: AppDatabase): PathogenDao = appDatabase.pathogenDao()

    @Singleton
    @Provides
    fun provideAlertDao(appDatabase: AppDatabase): AlertDao = appDatabase.alertDao()
}