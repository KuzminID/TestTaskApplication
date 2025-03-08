package com.example.testtaskapplication.di

import android.app.Application
import com.example.testtaskapplication.data.local.NewsDao
import com.example.testtaskapplication.data.remote.ApiService
import com.example.testtaskapplication.data.remote.RequestApi
import com.example.testtaskapplication.data.repositories.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideApp() = application

    @Singleton
    @Provides
    fun provideNewsRepository(
        newsDao: NewsDao,
        apiService: ApiService
    ) = NewsRepositoryImpl(
        newsDao, apiService
    )

    @Singleton
    @Provides
    fun provideContext(application: Application) = application.applicationContext

    @Singleton
    @Provides
    fun provideAPiService(requestApi: RequestApi): ApiService {
        return ApiService(requestApi)
    }
}