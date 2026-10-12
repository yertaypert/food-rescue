package com.yertaypert.foodrescue.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yertaypert.foodrescue.data.SampleData
import com.yertaypert.foodrescue.ui.screens.BusinessSignUpScreen
import com.yertaypert.foodrescue.ui.screens.DetailScreen
import com.yertaypert.foodrescue.ui.screens.FeedScreen
import com.yertaypert.foodrescue.ui.screens.LoginScreen
import com.yertaypert.foodrescue.ui.screens.SignUpScreen

object Routes {
    const val FEED = "feed"
    const val DETAIL = "detail/{listingId}"
    const val LOGIN = "login"
    const val SIGNUP_USER = "signup_user"
    const val SIGNUP_GIVER = "signup_giver"
    const val PROFILE = "profile"
    fun detail(id: Int) = "detail/$id"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.FEED) {
        composable(Routes.FEED) {
            FeedScreen(
                listings = SampleData.listings,
                onListingClick = { id -> navController.navigate(Routes.detail(id)) },
                // Logged out for now, so Profile leads to Login.
                // Later: if (isLoggedIn) Routes.PROFILE else Routes.LOGIN
                onProfileClick = { navController.navigate(Routes.LOGIN) }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("listingId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("listingId") ?: -1
            DetailScreen(
                listingId = id,
                onBack = { navController.popBackStack() },
                onLogin = { navController.navigate(Routes.LOGIN) },
                onRegisterUser = { navController.navigate(Routes.SIGNUP_USER) },
                onRegisterGiver = { navController.navigate(Routes.SIGNUP_GIVER) }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { _, _ -> /* UI only for now */ },
                onForgotPassword = {},
                onRegisterUser = {
                    navController.navigate(Routes.SIGNUP_USER) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegisterGiver = {
                    navController.navigate(Routes.SIGNUP_GIVER) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.SIGNUP_USER) {
            SignUpScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { _, _, _ -> /* UI only for now */ },
                onLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGNUP_USER) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.SIGNUP_GIVER) {
            BusinessSignUpScreen(
                onBack = { navController.popBackStack() },
                onSubmit = { _, _, _, _, _ -> /* UI only for now */ },
                onLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGNUP_GIVER) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.PROFILE) {
            Text("Profile (placeholder)")
        }
    }
}