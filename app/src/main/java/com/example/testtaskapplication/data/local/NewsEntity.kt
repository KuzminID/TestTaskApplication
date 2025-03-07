package com.example.testtaskapplication.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.sql.Date
import java.time.LocalDate

@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey
    val id : Long,
    @ColumnInfo(name = "type")
    val type : Int,
    @ColumnInfo(name = "title")
    val title : String,
    @ColumnInfo(name = "img")
    val imageUrl : String,
    @ColumnInfo(name = "news_date")
    val publicationDate : String,
    @ColumnInfo(name = "news_date_uts")
    val publicationDateUts : Long,
    @ColumnInfo(name = "annotation")
    val annotation : String,
    @ColumnInfo(name = "mobile_url")
    val mobileUrl : String,
    @ColumnInfo(name = "ignore_state")
    val isIgnored : Boolean = false
)
