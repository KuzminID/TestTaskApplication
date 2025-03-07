package com.example.testtaskapplication.di

import com.example.testtaskapplication.data.repositories.NewsRepositoryImpl
import dagger.Component
import jakarta.inject.Singleton

@Component(modules = [AppModule::class, RetrofitModule::class, RoomModule::class])
@Singleton
interface AppComponent {

    fun getNewsRepositoryImpl(): NewsRepositoryImpl


}