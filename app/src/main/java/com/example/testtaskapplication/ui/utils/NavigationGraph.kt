package com.example.testtaskapplication.ui.utils

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.testtaskapplication.domain.Screen
import com.example.testtaskapplication.domain.Screen.WebViewScreen
import com.example.testtaskapplication.ui.views.news.NewsScreen
import com.example.testtaskapplication.ui.views.news.NewsViewModel
import com.example.testtaskapplication.ui.views.web_view.WebViewScreen

@Composable
fun NavigationGraph(navController : NavHostController) {
    NavHost(
        navController,
        startDestination = Screen.NewsScreen.route
    ) {
        composable(
            route = Screen.NewsScreen.route
        ) {
            NewsScreen(
                navController = navController,
                viewModel()
            )
        }
        composable(
            route = "web_view/{url}",
            arguments = listOf(navArgument("url") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url")
            if (encodedUrl != null) {
                val decodedUrl = Uri.decode(encodedUrl)
                WebViewScreen(url = decodedUrl, navController = navController)
            }
        }
    }
}