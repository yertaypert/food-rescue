package com.yertaypert.foodrescue.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yertaypert.foodrescue.presentation.screens.BusinessSignUpRoute
import com.yertaypert.foodrescue.presentation.screens.DetailRoute
import com.yertaypert.foodrescue.presentation.screens.FeedRoute
import com.yertaypert.foodrescue.presentation.screens.LoginRoute
import com.yertaypert.foodrescue.presentation.screens.SignUpRoute

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
            FeedRoute(
                onListingClick = { id -> navController.navigate(Routes.detail(id)) },
                onProfileClick = { navController.navigate(Routes.LOGIN) }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("listingId") { type = NavType.IntType })
        ) {
            DetailRoute(
                onBack = { navController.popBackStack() },
                onLogin = { navController.navigate(Routes.LOGIN) },
                onRegisterUser = { navController.navigate(Routes.SIGNUP_USER) },
                onRegisterGiver = { navController.navigate(Routes.SIGNUP_GIVER) }
            )
        }
        composable(Routes.LOGIN) {
            LoginRoute(
                onBack = { navController.popBackStack() },
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
                },
                onSignedIn = { navController.popBackStack() }
            )
        }
        composable(Routes.SIGNUP_USER) {
            SignUpRoute(
                onBack = { navController.popBackStack() },
                onLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGNUP_USER) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onAccountCreated = { navController.popBackStack() }
            )
        }
        composable(Routes.SIGNUP_GIVER) {
            BusinessSignUpRoute(
                onBack = { navController.popBackStack() },
                onLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGNUP_GIVER) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onAccountCreated = { navController.popBackStack() }
            )
        }
        composable(Routes.PROFILE) {
            Text("Profile (placeholder)")
        }
    }
}
