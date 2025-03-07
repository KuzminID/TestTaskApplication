package com.example.testtaskapplication.data.remote

import com.example.testtaskapplication.data.local.NewsEntity
import jakarta.inject.Inject

class ApiService @Inject constructor(private val requestApi: RequestApi) {

    suspend fun getNewsFromApi () : List<NewsEntity> {
        return requestApi.getNewsList()
    }
}