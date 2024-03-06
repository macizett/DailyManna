package com.ketchup.dailymanna.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.ketchup.dailymanna.ui.screens.FavoritesScreen
import com.ketchup.dailymanna.ui.screens.MainScreen
import com.ketchup.dailymanna.ui.screens.SelectorScreen
import com.ketchup.dailymanna.viewmodel.MannaViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavController(mannaViewModel: MannaViewModel = koinViewModel()) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "MainScreen"){
        composable("MainScreen") { MainScreen(
            navController = navController,
            mannaViewModel = mannaViewModel,
            initialPage =  mannaViewModel.getSavedPageIndex()
        )}

        composable("FavoritesScreen") {
            FavoritesScreen(mannaViewModel, navController)
        }

        composable("SelectorScreen") {
            SelectorScreen(mannaViewModel, navController)
        }
    }
}