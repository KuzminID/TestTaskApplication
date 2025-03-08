package com.example.testtaskapplication.ui.views.news

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.testtaskapplication.data.local.NewsEntity
import com.example.testtaskapplication.domain.Screen

@Composable
fun NewsScreen(navController: NavController, viewModel: NewsViewModel) {
    val newsList by viewModel.newsList.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { viewModel.searchNews(searchQuery) },
            onClear = { searchQuery = "" },
            onRefresh = {
                viewModel.loadFullNewsList()
            }
        )
        NewsList(newsList = newsList, onIgnoreClick = { news ->
            viewModel.toggleIgnoreNews(news)
        }, onItemClick = { news ->
            val url = Uri.encode(news.mobileUrl)
            navController.navigate("web_view/$url")
        })
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onRefresh: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Box(Modifier.weight(1f)) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = onClear,
                            modifier = Modifier.padding(end = 25.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch() },
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onRefresh,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "Refresh Button")
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsList(
    newsList: List<NewsEntity>,
    onIgnoreClick: (NewsEntity) -> Unit,
    onItemClick: (NewsEntity) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(newsList) { news ->
            if (!news.isIgnored) {
                NewsItem(news = news, onIgnoreClick = onIgnoreClick, onItemClick = onItemClick)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(
    news: NewsEntity,
    onIgnoreClick: (NewsEntity) -> Unit,
    onItemClick: (NewsEntity) -> Unit
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
                        .align(Alignment.TopEnd), // Кнопка "Скрыть" в правом верхнем углу
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = if (!news.isIgnored) "Скрыть" else "Показать",
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = news.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = news.annotation, style = MaterialTheme.typography.bodySmall)
            Button(onClick = { onItemClick(news) }) {
                Text(text = "Читать далее")
            }
        }
    }
}