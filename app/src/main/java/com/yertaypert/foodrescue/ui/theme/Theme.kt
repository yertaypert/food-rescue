package com.yertaypert.foodrescue.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = FoodOrange,
    onPrimary = FoodWhite,

    secondary = FoodOrangeDark,
    onSecondary = FoodWhite,

    tertiary = FoodOrangeLight,
    onTertiary = FoodText,

    background = FoodBackground,
    onBackground = FoodText,

    surface = FoodWhite,
    onSurface = FoodText
)

private val DarkColorScheme = darkColorScheme(
    primary = FoodOrange,
    onPrimary = FoodWhite,

    secondary = FoodOrangeDark,
    onSecondary = FoodWhite,

    tertiary = FoodOrangeLight,
    onTertiary = FoodText,

    background = FoodText,
    onBackground = FoodWhite,

    surface = FoodText,
    onSurface = FoodWhite
)

@Composable
fun FoodRescueTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}