package com.example.testtaskapplication.data.remote

import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.domain.repositories.ApiResponse
import jakarta.inject.Inject

class ApiService @Inject constructor(private val requestApi: RequestApi) {

    suspend fun getNewsFromApi(): ApiResponse {
        return requestApi.getNewsList()
    }
}