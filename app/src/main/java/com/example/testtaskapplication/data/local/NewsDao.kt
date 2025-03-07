package com.example.testtaskapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update

@Dao
interface NewsDao {
    @Insert
    fun insertNews(newsList : List<NewsEntity>)

    @Update
    fun updateNewsItem(newsItem : NewsEntity)

    @Update
    fun updateAllNews(newsList : List<NewsEntity>)
}