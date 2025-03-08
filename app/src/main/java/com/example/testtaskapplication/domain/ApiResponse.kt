package com.example.testtaskapplication.domain

import com.example.testtaskapplication.data.local.NewsEntity

data class ApiResponse(
    val success: Boolean,
    val data: Data
)

data class Data(
    val news: List<NewsEntity>
)
