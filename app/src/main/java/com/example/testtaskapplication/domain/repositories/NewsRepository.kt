package com.example.testtaskapplication.domain.repositories

import com.example.testtaskapplication.data.local.NewsEntity
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getAllNewsFromDb() : List<NewsEntity>

    suspend fun getAllNewsFromApi() : List<NewsEntity>

    suspend fun ignoreNews(newsItem : NewsEntity)

}