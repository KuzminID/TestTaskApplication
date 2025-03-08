package com.example.testtaskapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "news_table")
@Json(name = "news")
data class NewsEntity(
    @PrimaryKey
    val id: Long,
    @Json(name = "type")
    val type: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "img")
    val imageUrl: String,
    @Json(name = "news_date")
    val publicationDate: String,
    @Json(name = "news_date_uts")
    val publicationDateUts: Long,
    @Json(name = "annotation")
    val annotation: String,
    @Json(name = "mobile_url")
    val mobileUrl: String,
    @Json(name = "ignore_state")
    var isIgnored: Boolean = false
)
