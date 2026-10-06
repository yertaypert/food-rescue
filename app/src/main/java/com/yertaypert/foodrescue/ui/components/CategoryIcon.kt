package com.yertaypert.foodrescue.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yertaypert.foodrescue.data.Category
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme

@Composable
fun Category.containerColor(): Color = when (this) {
    Category.Bakery, Category.Dairy -> MaterialTheme.colorScheme.tertiaryContainer
    Category.Produce, Category.Drinks -> MaterialTheme.colorScheme.secondaryContainer
    Category.Meals -> MaterialTheme.colorScheme.primaryContainer
}

@Composable
fun Category.contentColor(): Color = when (this) {
    Category.Bakery, Category.Dairy -> MaterialTheme.colorScheme.onTertiaryContainer
    Category.Produce, Category.Drinks -> MaterialTheme.colorScheme.onSecondaryContainer
    Category.Meals -> MaterialTheme.colorScheme.onPrimaryContainer
}

@Composable
fun CategoryIcon(
    category: Category,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(MaterialTheme.shapes.medium)
            .background(category.containerColor()),
        contentAlignment = Alignment.Center
    ) {
        // Decorative: the category name is always shown next to it.
        Icon(
            imageVector = category.icon,
            contentDescription = null,
            tint = category.contentColor()
        )
    }
}

@Preview
@Composable
private fun CategoryIconPreview() {
    FoodRescueTheme { CategoryIcon(Category.Bakery) }
}