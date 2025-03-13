package com.example.testtaskapplication.ui.views.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtaskapplication.TestTaskApplication.Companion.appComponent
import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.data.repositories.NewsRepositoryImpl
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel() : ViewModel() {
    @Inject
    lateinit var repositoryImpl: NewsRepositoryImpl

    private val _newsList = MutableStateFlow(emptyList<NewsEntity>())
    val newsList: StateFlow<List<NewsEntity>> = _newsList
    private lateinit var newsListBackup : List<NewsEntity>

    init {
        appComponent.inject(this)
        viewModelScope.launch(Dispatchers.IO) {
            _newsList.value = repositoryImpl.getAllNews()
        }
    }

    fun searchNews(query: String) {
        newsListBackup = _newsList.value /*Not using if..else because
                                           current news is constantly changing
                                           and verification using if..else would be an unoptimized.*/
        viewModelScope.launch(Dispatchers.Default) {
            val filteredNews =
                _newsList.value.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.annotation.contains(query, ignoreCase = true)
                }
            _newsList.value = filteredNews
        }
    }

    /**
    loadState = false for using newsList from backup; loadState = true for loading newsList from api
    */
    fun loadFullNewsList(loadState : Boolean) {
        if (loadState) {
            viewModelScope.launch(Dispatchers.IO) {
                _newsList.value = repositoryImpl.getAllNews()
            }
        } else {
            _newsList.value = newsListBackup
        }
    }

    fun toggleIgnoreNews(news: NewsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryImpl.ignoreNews(news)
            _newsList.value = repositoryImpl.getAllNews()
        }
    }
}
