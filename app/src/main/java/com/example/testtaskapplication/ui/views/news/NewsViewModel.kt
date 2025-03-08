package com.example.testtaskapplication.ui.views.news

import androidx.compose.ui.platform.DisableContentCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testtaskapplication.TestTaskApplication.Companion.appComponent
import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.data.repositories.NewsRepositoryImpl
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext

class NewsViewModel () : ViewModel() {
    @Inject
    lateinit var repositoryImpl: NewsRepositoryImpl

    private val _newsList = MutableStateFlow(emptyList<NewsEntity>())
    val newsList: StateFlow<List<NewsEntity>> = _newsList

    init {
        appComponent.inject(this)
        viewModelScope.launch(Dispatchers.IO) {
            _newsList.value = repositoryImpl.getAllNews()
        }
    }

    fun searchNews(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val filteredNews =
                _newsList.value.filter {
                    it.title.contains(query, ignoreCase = true) ||
                    it.annotation.contains(query, ignoreCase = true)
                }
            _newsList.value = filteredNews
        }
    }

    fun loadFullNewsList() {
        viewModelScope.launch(Dispatchers.IO) {
            _newsList.value = repositoryImpl.getAllNews()
        }
    }

    fun toggleIgnoreNews(news: NewsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryImpl.ignoreNews(news)
            _newsList.value = repositoryImpl.getAllNews()
        }
    }
}
