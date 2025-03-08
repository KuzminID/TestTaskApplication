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

        try {
            val apiNews = apiService.getNewsFromApi().data.news
            val updatedNews = mergeAndUpdate(dbNews, apiNews)
            newsDao.insertNews(updatedNews)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return newsDao.getAllNews()
    }

    private fun mergeAndUpdate(
        dbNews: List<NewsEntity>,
        apiNews: List<NewsEntity>
    ): List<NewsEntity> {
        val ignoredNews = dbNews.filter { it.isIgnored }
        ignoredNews.forEach { item ->
            apiNews.find { it.id == item.id }?.isIgnored = true
        }
        return apiNews
    }

    override suspend fun ignoreNews(newsItem: NewsEntity) {
        newsItem.isIgnored = true
        newsDao.updateNewsItem(newsItem)
    }
}