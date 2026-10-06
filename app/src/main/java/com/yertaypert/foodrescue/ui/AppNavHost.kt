package com.yertaypert.foodrescue.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yertaypert.foodrescue.data.SampleData
import com.yertaypert.foodrescue.ui.screens.FeedScreen

object Routes {
    const val FEED = "feed"
    const val DETAIL = "detail/{listingId}"
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
                onProfileClick = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("listingId") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("listingId")
            Text("Detail placeholder, id = $id") // replaced in next step
        }
        composable(Routes.PROFILE) {
            Text("Profile placeholder") // replaced later
        }
    }
}