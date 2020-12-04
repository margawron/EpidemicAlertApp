package com.github.margawron.epidemicalertapp.di

import android.content.Context
import com.github.margawron.epidemicalertapp.util.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UtilsModule {

    @Singleton
    @Provides
    fun providePreferenceHelper(@ApplicationContext context: Context): PreferenceHelper = PreferenceHelper(context)

}