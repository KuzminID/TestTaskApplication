package com.example.testtaskapplication.ui.views.news

import android.net.Uri
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.testtaskapplication.data.local.NewsEntity
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(navController: NavController, viewModel: NewsViewModel) {
    val newsList by viewModel.newsList.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val status by viewModel.uiState.collectAsState()
    val swipeRefreshState by remember {mutableStateOf(false)}
    val newsListState by viewModel.newsListState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новости") },
                actions = {
                    when (newsListState) {
                        NewsViewModel.NewsListState.IgnoredNews -> {
                            IconButton(onClick = { viewModel.loadUnignoredNews(true) }) {
                                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                            }
                        }
                        NewsViewModel.NewsListState.UnignoredNews -> {
                            IconButton(onClick = { viewModel.loadIgnoredNews(true) }) {
                                Icon(Icons.Filled.Delete, contentDescription = null)
                            }
                        }
                    }
                    IconButton(onClick = { viewModel.loadFullNewsList(true) }) {
                        Icon(Icons.Filled.Refresh, contentDescription = null)
                    }
                }
            )
        },
        content = { innerPadding ->

            when (status) {
                NewsViewModel.LoadingState.Loading ->  {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                        Text("Загрузка списка новостей", modifier = Modifier.padding(top = 75.dp))
                    }
                }
                NewsViewModel.LoadingState.Success -> {

                    Column(Modifier.padding(innerPadding)) {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onSearch = { viewModel.searchNews(searchQuery) },
                            onClear = { searchQuery = ""
                                /*Implementation of the functionality to return
                                 the news list to its original value without loading from api*/
                                        viewModel.loadFullNewsList(false)}
                        )
                        NewsList(
                            newsList = newsList,
                            onIgnoreClick = { news ->
                                viewModel.toggleIgnoreNews(news)
                            },
                            onItemClick = { news ->
                                val url = Uri.encode(news.mobileUrl)
                                navController.navigate("web_view/$url")
                            },
                            isRefreshing = swipeRefreshState,
                            onRefresh = {
                                viewModel.refreshNews()
                            },
                            when (newsListState) {
                                NewsViewModel.NewsListState.IgnoredNews -> Icons.AutoMirrored.Default.ArrowBack
                                NewsViewModel.NewsListState.UnignoredNews -> Icons.Default.Close
                            }
                        )
                    }
                }
                NewsViewModel.LoadingState.Error -> {
                    OnErrorView(
                        "Ошибка при загрузке данных. Проверьте соединение с интернетом",
                        "Повторить попытку",
                        action = { viewModel.loadFullNewsList(true) }
                    )
                }

                NewsViewModel.LoadingState.EmptyIgnored -> {
                    OnErrorView(
                        "Список скрытых новостей пуст",
                        "Вернуться ко всем новостям",
                        action = { viewModel.loadUnignoredNews(false) }
                    )
                }

                NewsViewModel.LoadingState.EmptyUnignored -> {
                    OnErrorView(
                        "Список новостей пуст",
                        "Посмотреть скрытые",
                        action = { viewModel.loadIgnoredNews(false) }
                    )
                }
            }
        }
    )
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Box(Modifier.weight(1f)) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = onClear
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch() },
                    onDone = {
                        //Using focus manager for keyboard hiding
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun OnErrorView(errorText : String, buttonText : String, action : () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = errorText,
            modifier = Modifier
                .padding(bottom = 100.dp),
            textAlign = TextAlign.Center)
        Button(onClick = action) {
            Text(buttonText, Modifier)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsList(
    newsList: List<NewsEntity>,
    onIgnoreClick: (NewsEntity) -> Unit,
    onItemClick: (NewsEntity) -> Unit,
    isRefreshing : Boolean,
    onRefresh : () -> Unit,
    ignoredStateIcon : ImageVector
)
{
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh,
        indicator = { state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                scale = true,
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(newsList) { news ->
                NewsItem(news = news, onIgnoreClick = onIgnoreClick, onItemClick = onItemClick,ignoredStateIcon)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(
    news: NewsEntity,
    onIgnoreClick: (NewsEntity) -> Unit,
    onItemClick: (NewsEntity) -> Unit,
    ignoredStateIcon : ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                GlideImage(
                    model = news.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                IconButton(
                    onClick = { onIgnoreClick(news) },
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.TopEnd),
                ) {
                    Icon(
                        ignoredStateIcon,
                        contentDescription = if (!news.isIgnored) "Скрыть" else "Показать",
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = news.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = news.annotation, style = MaterialTheme.typography.bodyMedium)
            Button(onClick = { onItemClick(news) }) {
                Text(text = "Читать далее")
            }
        }
    }
}