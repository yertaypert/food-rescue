package com.yertaypert.foodrescue.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yertaypert.foodrescue.presentation.screens.BusinessSignUpRoute
import com.yertaypert.foodrescue.presentation.screens.DetailRoute
import com.yertaypert.foodrescue.presentation.screens.FeedRoute
import com.yertaypert.foodrescue.presentation.screens.LoginRoute
import com.yertaypert.foodrescue.presentation.screens.ProfileRoute
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
    val sessionViewModel: SessionViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Routes.FEED) {
        composable(Routes.FEED) {
            FeedRoute(
                onListingClick = { id -> navController.navigate(Routes.detail(id)) },
                onProfileClick = {
                    val destination = if (sessionViewModel.isSignedIn()) {
                        Routes.PROFILE
                    } else {
                        Routes.LOGIN
                    }
                    navController.navigate(destination)
                }
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
                        launchSingleTop = true
                    }
                },
                onRegisterGiver = {
                    navController.navigate(Routes.SIGNUP_GIVER) {
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
                onAccountCreated = {
                    when (navController.previousBackStackEntry?.destination?.route) {
                        Routes.DETAIL -> navController.popBackStack()
                        else -> navController.navigate(Routes.FEED) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
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
                onAccountCreated = {
                    when (navController.previousBackStackEntry?.destination?.route) {
                        Routes.DETAIL -> navController.popBackStack()
                        else -> navController.navigate(Routes.FEED) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
        composable(Routes.PROFILE) {
            ProfileRoute(
                onBack = { navController.popBackStack() },
                onSignedOut = {
                    // Return to the feed as a guest; it is always browsable.
                    if (!navController.popBackStack()) {
                        navController.navigate(Routes.FEED) {
                            popUpTo(Routes.PROFILE) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
