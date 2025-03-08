package com.example.testtaskapplication.data.remote

import com.example.testtaskapplication.domain.ApiResponse
import jakarta.inject.Inject

class ApiService @Inject constructor(private val requestApi: RequestApi) {

    suspend fun getNewsFromApi(): ApiResponse {
        return requestApi.getNewsList()
    }
}