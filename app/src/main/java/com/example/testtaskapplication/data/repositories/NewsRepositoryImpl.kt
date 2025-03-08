package com.example.testtaskapplication.data.repositories

import com.example.testtaskapplication.data.local.NewsDao
import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.data.remote.ApiService
import jakarta.inject.Inject

interface NewsRepository {

    suspend fun getAllNews(): List<NewsEntity>

    suspend fun ignoreNews(newsItem: NewsEntity)

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
            val updatedNews = mergeAndUpdate(dbNews, apiNews)
            newsDao.insertNews(updatedNews)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val unIgnoredNews : List<NewsEntity> = newsDao.getAllNews().filter { !it.isIgnored }
        return sortByDescendingDate(unIgnoredNews)
    }

    private fun sortByDescendingDate(newsList : List<NewsEntity>) : List<NewsEntity> {
        return newsList.sortedByDescending { it.publicationDateUts }
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