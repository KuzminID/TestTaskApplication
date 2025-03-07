package com.example.testtaskapplication.data.repositories

import com.example.testtaskapplication.data.local.NewsDao
import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.data.remote.ApiService
import com.example.testtaskapplication.domain.repositories.NewsRepository
import jakarta.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao : NewsDao,
    private val apiService: ApiService
) : NewsRepository {

    override suspend fun getAllNewsFromDb(): List<NewsEntity> {
        return newsDao.getAllNews()
    }

    override suspend fun getAllNewsFromApi(): List<NewsEntity> {
        return emptyList() //TODO add getting news from api and
                            // changing its isIgnored field value as dbNews.isIgnored
    }

    override suspend fun ignoreNews(newsItem: NewsEntity) {
        newsItem.isIgnored = true
        newsDao.updateNewsItem(newsItem)
    }
}