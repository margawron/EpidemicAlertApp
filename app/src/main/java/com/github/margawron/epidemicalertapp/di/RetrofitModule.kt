package com.github.margawron.epidemicalertapp.di

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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://ostrzezenieepidemiologiczne.tk/api/")
        .addCallAdapterFactory(ApiResponseCallAdapter())
        .addConverterFactory(JacksonConverterFactory.create())
        .client(okHttpClient)
        .build()
}