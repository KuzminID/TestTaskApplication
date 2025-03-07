package com.example.testtaskapplication.data.repositories

import android.util.Log
import com.example.testtaskapplication.data.local.NewsDao
import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.data.remote.ApiService
import com.example.testtaskapplication.domain.repositories.NewsRepository
import jakarta.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val apiService: ApiService
) : NewsRepository {

    override suspend fun getAllNewsFromDb(): List<NewsEntity> {
        val news = getAllNewsFromApi()
        return news
    }

    override suspend fun getAllNewsFromApi(): List<NewsEntity> {
        val apiNews = apiService.getNewsFromApi().data.news
        val dbNews = newsDao.getAllNews()
        val updatedNews = mutableListOf<NewsEntity>()
        apiNews.forEach { item ->
            val existingItem = dbNews.find { item.id == it.id }
            if (existingItem == null) {
                updatedNews.add(item)
            } else {
                if (existingItem.isIgnored != item.isIgnored) {
                    item.isIgnored = true
                    updatedNews.add(item)
                }
            }
        }
        newsDao.insertNews(updatedNews)
        return updatedNews
    }

    override suspend fun ignoreNews(newsItem: NewsEntity) {
        newsItem.isIgnored = true
        newsDao.updateNewsItem(newsItem)
    }
}