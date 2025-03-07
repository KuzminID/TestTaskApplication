package com.example.testtaskapplication.domain.repositories

import com.example.testtaskapplication.data.local.NewsEntity

interface NewsRepository {

    suspend fun getAllNews(): List<NewsEntity>

    suspend fun ignoreNews(newsItem: NewsEntity)

}