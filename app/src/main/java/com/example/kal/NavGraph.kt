package com.example.kal

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kal.ui.screen.MainScreen
import com.example.kal.ui.screen.FavoriteScreen
import com.example.kal.ui.screen.ResponsesScreen
import com.example.kal.ui.screen.ProfileScreen
import android.view.View
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.kal.db.vacancy.Vacancy
import com.example.kal.ui.screen.ActiveSearchScreen
import com.example.kal.ui.screen.SignInScreen
import com.example.kal.ui.screen.SignUpForm
import com.example.kal.ui.screen.SignUpScreen
import com.example.kal.ui.screen.SplashScreen
import com.example.kal.ui.screen.VacancyInfoScreen
import com.example.kal.utils.SharedManager
import kotlinx.serialization.json.Json

@Composable
fun NavGraph(
    navController: NavHostController,
    isStatusBarIconsDark: MutableState<Boolean>,
    view: View
) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        enterTransition = { fadeIn(animationSpec = tween(0)) },
        exitTransition = { fadeOut(animationSpec = tween(0)) }
    ) {
        composable(route = "search") {
            MainScreen(isStatusBarIconsDark = isStatusBarIconsDark, view = view, navController = navController)
        }
        composable(route = "favorite") {
            FavoriteScreen(navController = navController)
        }
        composable(
            route = "vacancyInfo/{vacId}",
            arguments = listOf(navArgument("vacId") { type = NavType.IntType })
        ) { backStackEntry ->
            val vacId = backStackEntry.arguments?.getInt("vacId") ?: 0
            VacancyInfoScreen(
                vacId = vacId,
                navController = navController,
                isStatusBarIconsDark = isStatusBarIconsDark,
                view = view
            )
        }

        composable(route = "registration") {
            SignUpScreen(navController)
        }
        composable(route = "login") {
            SignInScreen(navController)
        }
        composable(route = "splash") {
            SplashScreen(navController)
        }
        composable(route = "responses") {
            ResponsesScreen(navController = navController)
        }
        composable(route = "profile") {
            ProfileScreen(navController = navController)
        }
        composable(route = "active_search") {
            ActiveSearchScreen(navController = navController)
        }
    }
}