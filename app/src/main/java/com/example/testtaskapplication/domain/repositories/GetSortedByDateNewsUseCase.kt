package com.example.testtaskapplication.domain.repositories

import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.data.repositories.NewsRepositoryImpl
import jakarta.inject.Inject

class GetSortedByDateNewsUseCase @Inject constructor(private val repository: NewsRepositoryImpl) {
    suspend operator fun invoke(): List<NewsEntity> {
        val allNews = repository.getAllNews()
        return allNews.sortedByDescending { it.publicationDateUts }
    }
}