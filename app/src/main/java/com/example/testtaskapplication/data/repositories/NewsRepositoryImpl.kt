package com.example.testtaskapplication.data.repositories

import com.example.testtaskapplication.data.local.NewsDao
import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.data.remote.ApiService
import jakarta.inject.Inject
import java.util.regex.Pattern

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
            val updatedNews = mergeNews(dbNews, apiNews)
            newsDao.insertNews(updatedNews)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val fixedMobileUrlNews = addLackPartOfUrl(newsDao.getAllNews())
        newsDao.insertNews(fixedMobileUrlNews)
        val unIgnoredNews: List<NewsEntity> = fixedMobileUrlNews.filter { !it.isIgnored }
        return sortByDescendingDate(unIgnoredNews)
    }

    private fun sortByDescendingDate(newsList: List<NewsEntity>): List<NewsEntity> {
        return newsList.sortedByDescending { it.publicationDateUts }
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

    //Fixing mobileUrl field which missing 'api' part after port and before path
    private fun addLackPartOfUrl(news: List<NewsEntity>): List<NewsEntity> {
        val pattern = Pattern.compile("(http?://[^:/]+:\\d+)")
        news.forEach { item ->
            val url = item.mobileUrl
            val matcher = pattern.matcher(url)

            if (matcher.find()) {
                val newUrl = url.replaceFirst(matcher.group(), "${matcher.group()}/api/")
                item.mobileUrl = newUrl
            }
        }
        return news
    }

    //Updating news isIgnored field for hiding news from showing in view
    override suspend fun ignoreNews(newsItem: NewsEntity) {
        newsItem.isIgnored = true
        newsDao.updateNewsItem(newsItem)
    }
}