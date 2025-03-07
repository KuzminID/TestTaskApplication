package com.example.testtaskapplication.di

import android.content.Context
import com.example.testtaskapplication.data.local.NewsDatabase
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(context: Context) = NewsDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideNewsDao(database: NewsDatabase) = database.getNewsDao()

}