package com.ketchup.dailymanna.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.ketchup.dailymanna.ui.screens.FavoritesScreen
import com.ketchup.dailymanna.ui.screens.MainScreen
import com.ketchup.dailymanna.ui.screens.SelectorScreen
import com.ketchup.dailymanna.viewmodel.ViewModel

@Composable
fun NavController(viewModel: ViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "MainScreen"){
        composable("MainScreen") { MainScreen(
            navController = navController,
            viewModel = viewModel,
            initialPage =  viewModel.getSavedPageIndex()
        )}

        composable("FavoritesScreen") {
            FavoritesScreen(viewModel, navController)
        }

        composable("SelectorScreen") {
            SelectorScreen(viewModel, navController)
        }
    }
}