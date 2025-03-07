package com.example.testtaskapplication

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.testtaskapplication.data.local.NewsDao
import com.example.testtaskapplication.data.local.NewsDatabase
import com.example.testtaskapplication.data.local.NewsEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var db: NewsDatabase
    private lateinit var newsDao: NewsDao

    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java).build()
        newsDao = db.getNewsDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertNewsTest() = runTest {
        val firstNews = NewsEntity(
            id = 0,
            type = 1,
            title = "FirstNewTile",
            imageUrl = "FirstNewImageUrl",
            publicationDate = LocalDate.now().toString(),
            publicationDateUts = 0,
            annotation = "FirstNewAnnotation",
            mobileUrl = "FirstNewMobileUrl",
            isIgnored = false
        )
        val secondNews = NewsEntity(
            id = 1,
            type = 1,
            title = "SecondNewTile",
            imageUrl = "SecondNewImageUrl",
            publicationDate = LocalDate.now().toString(),
            publicationDateUts = 0,
            annotation = "SecondNewAnnotation",
            mobileUrl = "SecondNewMobileUrl",
            isIgnored = false
        )
        val news = listOf<NewsEntity>(firstNews, secondNews)
        newsDao.insertNews(news)
        val dbNews = newsDao.getAllNews()
        assertEquals(news, dbNews)
    }
}