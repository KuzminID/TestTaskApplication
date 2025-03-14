package com.example.testtaskapplication.data.repositories

import com.example.testtaskapplication.data.local.NewsDao
import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.data.remote.ApiService
import jakarta.inject.Inject

interface NewsRepository {

    suspend fun getAllNews(): List<NewsEntity>

    suspend fun changeIgnoredState(newsItem: NewsEntity)

    suspend fun getAllNewsFromDb() : List<NewsEntity>
}

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val apiService: ApiService
) : NewsRepository {

    //Getting data from database and api and merging them by isIgnored fields
    //Then filtering it and sorting by publication date
    override suspend fun getAllNews(): List<NewsEntity> {
        var dbNews = newsDao.getAllNews()

        try {
            val apiNews = apiService.getNewsFromApi().data.news
            val updatedNews = mergeNews(dbNews, apiNews)
            newsDao.insertNews(updatedNews)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val finalNews = newsDao.getAllNews()

        return finalNews
    }

    //Merging api news with database news using database isIgnored field
    private fun mergeNews(
        dbNews: List<NewsEntity>,
        apiNews: List<NewsEntity>
    ): List<NewsEntity> {
        val ignoredNews = dbNews.filter { it.isIgnored }
        ignoredNews.forEach { item ->
            apiNews.find { it.id == item.id }?.isIgnored = true
        }
        return apiNews
    }

    //Updating news isIgnored field for hiding news from showing in view
    override suspend fun changeIgnoredState(newsItem: NewsEntity) {
        newsItem.isIgnored = !newsItem.isIgnored
        newsDao.updateNewsItem(newsItem)
    }

    override suspend fun getAllNewsFromDb(): List<NewsEntity> {
        try {
          val news = newsDao.getAllNews()
          return news
        } catch (e : Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}