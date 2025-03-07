package com.example.testtaskapplication.data.repositories

import com.example.testtaskapplication.data.local.NewsDao
import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.data.remote.ApiService
import com.example.testtaskapplication.domain.repositories.NewsRepository
import jakarta.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val apiService: ApiService
) : NewsRepository {

    override suspend fun getAllNews(): List<NewsEntity> {
        var dbNews = newsDao.getAllNews()

        val apiNews = apiService.getNewsFromApi().data.news

        val ignoredNews = dbNews.filter { it.isIgnored == true }
        ignoredNews.forEach { item ->
            apiNews.find { item.id == it.id }?.isIgnored = true

        }
        newsDao.insertNews(apiNews)

        return apiNews
    }

    override suspend fun ignoreNews(newsItem: NewsEntity) {
        newsItem.isIgnored = true
        newsDao.updateNewsItem(newsItem)
    }
}