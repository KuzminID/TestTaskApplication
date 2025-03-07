package com.example.testtaskapplication.di

import android.app.Application
import com.example.testtaskapplication.data.remote.ApiService
import com.example.testtaskapplication.data.remote.RequestApi
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext() = Application().applicationContext

    @Singleton
    @Provides
    fun provideAPiService(requestApi: RequestApi) : ApiService {
        return ApiService(requestApi)
    }

}