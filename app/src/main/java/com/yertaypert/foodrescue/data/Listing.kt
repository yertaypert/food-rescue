package com.yertaypert.foodrescue.data

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.RamenDining
import androidx.compose.ui.graphics.vector.ImageVector
import com.yertaypert.foodrescue.R

enum class Category(val label: String, val icon: ImageVector) {
    Bakery("Bakery", Icons.Outlined.BakeryDining),
    Produce("Produce", Icons.Outlined.Eco),
    Meals("Meals", Icons.Outlined.RamenDining),
    Drinks("Drinks", Icons.Outlined.LocalCafe),
    Dairy("Dairy", Icons.Outlined.LocalDrink)
}

data class Listing(
    val id: Int,
    val title: String,
    val giverName: String,
    val distanceKm: Double,
    val category: Category,
    val minutesLeft: Int,
    val description: String,
    val pickupWindow: String = "6:00 - 7:30 PM",
    val area: String = "Baker Street area",
    @DrawableRes val imageRes: Int = R.drawable.ic_food_hero
)