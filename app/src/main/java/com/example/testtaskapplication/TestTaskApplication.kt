package com.example.testtaskapplication

import android.app.Application
import com.example.testtaskapplication.di.AppComponent
import com.example.testtaskapplication.di.AppModule
import com.example.testtaskapplication.di.DaggerAppComponent
import com.example.testtaskapplication.di.RetrofitModule
import com.example.testtaskapplication.di.RoomModule

class TestTaskApplication : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .retrofitModule(RetrofitModule())
            .roomModule(RoomModule())
            .build()
    }
}