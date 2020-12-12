package com.github.margawron.epidemicalertapp.di

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.margawron.epidemicalertapp.api.ApiResponseCallAdapter
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
    fun provideObjectMapper(): ObjectMapper = jacksonObjectMapper()

    @Singleton
    @Provides
    fun provideRetrofit(objectMapper: ObjectMapper, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://ostrzezenieepidemiologiczne.tk/api/")
        .addCallAdapterFactory(ApiResponseCallAdapter())
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .client(okHttpClient)
        .build()
}