package com.example.notetest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notetest.ui.screens.HomeScreen
import com.example.notetest.ui.screens.EditorScreen
import com.example.notetest.ui.viewmodel.SharedViewModel

@Composable
fun AppNavigation(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable("editor/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull()
            EditorScreen(navController = navController, viewModel = sharedViewModel, noteId = noteId)
        }
    }
}

