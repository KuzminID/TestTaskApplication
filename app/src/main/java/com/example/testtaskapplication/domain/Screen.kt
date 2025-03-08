package com.example.testtaskapplication.domain

sealed class Screen(val route : String) {

    object NewsScreen : Screen(route = "news_screen")
    object WebViewScreen : Screen(route = "web_view")
}