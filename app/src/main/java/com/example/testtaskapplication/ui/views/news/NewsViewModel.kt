package com.example.testtaskapplication.ui.views.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtaskapplication.TestTaskApplication.Companion.appComponent
import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.data.repositories.NewsRepositoryImpl
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel() : ViewModel() {
    @Inject
    lateinit var repositoryImpl: NewsRepositoryImpl

    private val _newsList = MutableStateFlow(emptyList<NewsEntity>())
    val newsList: StateFlow<List<NewsEntity>> = _newsList
    private lateinit var newsListBackup : List<NewsEntity>

    private val _uiState = MutableStateFlow(LoadingState.Loading)
    val uiState : StateFlow<LoadingState> = _uiState

    init {
        appComponent.inject(this)
        viewModelScope.launch(Dispatchers.IO) {
            loadNewsFromApi()
        }
    }

    //Extracted to separate fun because of multiple using cases
    private suspend fun loadNewsFromApi() {
        try {
            _uiState.value = LoadingState.Loading
            _newsList.value = repositoryImpl.getAllNews()

            /*Added delay for showing user that loading is still processing in case
            of getting error occurs too fast and user only sees LoadingState.Error view state*/
            delay(1000)

            if (_newsList.value != emptyList<NewsEntity>()) {
                _uiState.value = LoadingState.Success
            } else {
                _uiState.value = LoadingState.Error
            }
        } catch (e : Exception) {
            _uiState.value = LoadingState.Error
            e.printStackTrace()
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
    loadFromApi = false for using newsList from backup; loadFromApi = true for loading newsList from api
    */
    fun loadFullNewsList(loadFromApi : Boolean) {
        if (loadFromApi) {
            viewModelScope.launch(Dispatchers.IO) {
                loadNewsFromApi()
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

    enum class LoadingState{
        Loading,
        Success,
        Error
    }
}
