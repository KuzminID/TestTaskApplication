package com.example.testtaskapplication.di

import android.app.Application
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext() = Application().applicationContext
}