package com.example.testtaskapplication.domain

//All available screens list and their routes
sealed class Screen(val route: String) {
    object NewsScreen : Screen(route = "news_screen")
    object WebViewScreen : Screen(route = "web_view")
}