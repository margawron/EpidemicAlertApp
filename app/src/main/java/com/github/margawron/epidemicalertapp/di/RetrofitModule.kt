package com.github.margawron.epidemicalertapp.di

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.github.margawron.epidemicalertapp.api.common.ApiResponseCallAdapter
import com.github.margawron.epidemicalertapp.api.location.LocationService
import com.github.margawron.epidemicalertapp.api.measurements.MeasurementService
import com.github.margawron.epidemicalertapp.api.users.UserService
import com.github.margawron.epidemicalertapp.api.zones.ZoneService
import com.github.margawron.epidemicalertapp.data.locations.LocationRepository
import com.github.margawron.epidemicalertapp.data.measurments.MeasurementRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun provideObjectMapper(): ObjectMapper =
        jacksonMapperBuilder()
            .addModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(objectMapper: ObjectMapper, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://ostrzezenieepidemiologiczne.tk/api/")
            .addCallAdapterFactory(ApiResponseCallAdapter())
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Singleton
    @Provides
    fun provideMeasurementService(retrofit: Retrofit): MeasurementService =
        retrofit.create(MeasurementService::class.java)

    @Singleton
    @Provides
    fun provideLocationService(retrofit: Retrofit): LocationService =
        retrofit.create(LocationService::class.java)

    @Singleton
    @Provides
    fun provideZoneService(retrofit: Retrofit): ZoneService =
        retrofit.create(ZoneService::class.java)
}