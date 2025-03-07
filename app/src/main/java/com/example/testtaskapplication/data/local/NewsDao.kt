package com.example.testtaskapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(newsList : List<NewsEntity>)

    @Update
    fun updateNewsItem(newsItem : NewsEntity)

    @Update
    fun updateAllNews(newsList : List<NewsEntity>)

    @Query("SELECT * FROM news_table")
    fun getAllNews() : List<NewsEntity>

}