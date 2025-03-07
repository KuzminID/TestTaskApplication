package com.example.testtaskapplication.data.remote

import com.example.testtaskapplication.data.local.NewsEntity
import retrofit2.http.GET

interface RequestApi {
    @GET
    suspend fun getNewsList() : List<NewsEntity>
}