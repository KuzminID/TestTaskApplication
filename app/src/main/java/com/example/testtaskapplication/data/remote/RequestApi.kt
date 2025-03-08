package com.example.testtaskapplication.data.remote

import com.example.testtaskapplication.domain.ApiResponse
import retrofit2.http.GET

interface RequestApi {
    @GET("/api/mobile/news/list")
    suspend fun getNewsList(): ApiResponse
}