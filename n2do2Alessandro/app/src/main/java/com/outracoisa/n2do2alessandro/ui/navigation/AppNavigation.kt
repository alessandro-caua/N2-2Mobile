package com.outracoisa.n2do2alessandro.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.outracoisa.n2do2alessandro.helper.LolBlocker
import com.outracoisa.n2do2alessandro.ui.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddGame : Screen("add_game")
    object Details : Screen("details/{sessionId}") {
        fun createRoute(sessionId: Int) = "details/$sessionId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val context = LocalContext.current
    var isBlocked by remember { mutableStateOf(LolBlocker.isUserBlocked(context)) }
    var blockTimeRemaining by remember { mutableStateOf("") }
    
    // Update block status periodically
    LaunchedEffect(Unit) {
        while (true) {
            isBlocked = LolBlocker.isUserBlocked(context)
            if (isBlocked) {
                blockTimeRemaining = LolBlocker.formatBlockTime(
                    LolBlocker.getBlockTimeRemaining(context)
                )
            }
            kotlinx.coroutines.delay(1000) // Check every second
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAddClick = {
                    if (!LolBlocker.isUserBlocked(context)) {
                        navController.navigate(Screen.AddGame.route)
                    }
                },
                onSessionClick = { sessionId ->
                    navController.navigate(Screen.Details.createRoute(sessionId))
                },
                isBlocked = isBlocked
            )
        }
        
        composable(Screen.AddGame.route) {
            if (isBlocked) {
                BlockedScreen(
                    timeRemaining = blockTimeRemaining,
                    onTimeExpired = {
                        navController.popBackStack()
                    }
                )
            } else {
                AddGameScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onGameBlocked = {
                        isBlocked = true
                        navController.popBackStack()
                    }
                )
            }
        }
        
        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument("sessionId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getInt("sessionId") ?: 0
            DetailsScreen(
                sessionId = sessionId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
