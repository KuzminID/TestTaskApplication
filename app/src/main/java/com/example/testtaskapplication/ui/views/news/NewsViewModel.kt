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
    private var newsListBackup : List<NewsEntity> = emptyList()

    private val _uiState = MutableStateFlow(LoadingState.Loading)
    val uiState : StateFlow<LoadingState> = _uiState

    private val _newsListState = MutableStateFlow(NewsListState.UnignoredNews)
    val newsListState : StateFlow<NewsListState> = _newsListState

    init {
        appComponent.inject(this)
        viewModelScope.launch(Dispatchers.IO) {
            //Always loading news from api on init
            loadNewsFromApi(true)
        }
    }

    //Extracted to separate fun because of multiple using cases
    private suspend fun loadNewsFromApi(loadFromApiState : Boolean) {
        try {
            val newsList = if (loadFromApiState) {
                repositoryImpl.getAllNews()
            } else {
                repositoryImpl.getAllNewsFromDb()
            }

            if (newsList != emptyList<NewsEntity>()) {
                filterNewsByIgnoredState(newsList)
                //Delay for finishing filter operation
                delay(500)
                if (_newsList.value.isNotEmpty()) {
                    _uiState.value = LoadingState.Success
                } else if (_newsListState.value == NewsListState.IgnoredNews) {
                    _uiState.value = LoadingState.EmptyIgnored
                } else {
                    _uiState.value = LoadingState.EmptyUnignored
                }
            } else{
                _uiState.value = LoadingState.Error
            }
        } catch (e : Exception) {
            _uiState.value = LoadingState.Error
            e.printStackTrace()
        }
    }

    fun filterNewsByIgnoredState(newsList : List<NewsEntity>) {
        var filteredList = emptyList<NewsEntity>()
        viewModelScope.launch(Dispatchers.Default) {
            filteredList = when (_newsListState.value) {
                NewsListState.IgnoredNews -> newsList.filter { it.isIgnored }
                NewsListState.UnignoredNews -> newsList.filter { !it.isIgnored }
            }
            _newsList.value = sortNewsByDescendingDate(filteredList)
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
    fun loadFullNewsList(loadFromApiState : Boolean) {
        if (loadFromApiState) {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.value = LoadingState.Loading
                loadNewsFromApi(loadFromApiState)
            }
        } else {
            if (newsListBackup.isNotEmpty()) {
                _newsList.value = newsListBackup
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiState.value = LoadingState.Loading
                    loadNewsFromApi(loadFromApiState)
                }
            }
        }
    }

    fun loadUnignoredNews(loadFromApiState : Boolean) {
        _newsListState.value = NewsListState.UnignoredNews
        viewModelScope.launch(Dispatchers.IO) {
            loadNewsFromApi(loadFromApiState)
        }
    }

    fun loadIgnoredNews(loadFromApiState : Boolean) {
        _newsListState.value = NewsListState.IgnoredNews
        viewModelScope.launch(Dispatchers.IO) {
            loadNewsFromApi(loadFromApiState)
        }
    }

    private fun sortNewsByDescendingDate(newsList : List<NewsEntity>) : List<NewsEntity> {
        return newsList.sortedByDescending { it.publicationDateUts }
    }

    /*Using separate function for not changing uiState (This
    is needed for not hiding newsList from view*/
    fun refreshNews() {
        viewModelScope.launch(Dispatchers.IO) {
            //On refresh always trying to get news from api
            loadNewsFromApi(true)
        }
    }

    fun toggleIgnoreNews(news: NewsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryImpl.changeIgnoredState(news)
        }
    }

    enum class LoadingState{
        Loading,
        Success,
        Error,
        EmptyIgnored,
        EmptyUnignored
    }

    enum class NewsListState {
        IgnoredNews,
        UnignoredNews
    }
}
