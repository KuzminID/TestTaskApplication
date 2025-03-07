package com.example.testtaskapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [NewsEntity::class],
    version = 1
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao

    companion object {
        const val DB_NAME = "news_database.db"

        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java,
                        DB_NAME
                    ).build()

                    INSTANCE = instance

                }
                return instance
            }
        }
    }
}