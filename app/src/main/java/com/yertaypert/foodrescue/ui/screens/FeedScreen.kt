package com.yertaypert.foodrescue.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yertaypert.foodrescue.data.Category
import com.yertaypert.foodrescue.data.Listing
import com.yertaypert.foodrescue.ui.components.CategoryChip
import com.yertaypert.foodrescue.ui.components.EmptyState
import com.yertaypert.foodrescue.ui.components.EndingSoonCard
import com.yertaypert.foodrescue.ui.components.SectionHeader
import com.yertaypert.foodrescue.ui.theme.Spacing
import com.yertaypert.foodrescue.data.SampleData
import com.yertaypert.foodrescue.ui.components.ListingCard
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    listings: List<Listing>,
    onListingClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    onNotificationsClick: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    val filtered = remember(listings, selectedCategory) {
        listings.filter { selectedCategory == null || it.category == selectedCategory }
    }
    val endingSoon = remember(filtered) { filtered.sortedBy { it.minutesLeft }.take(5) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.LocationOn, contentDescription = null)
                        Text(
                            text = "Almaty · 2 km",
                            modifier = Modifier.padding(start = Spacing.sm),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNotificationsClick) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Outlined.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                    label = { Text("Feed") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Outlined.AddCircleOutline, contentDescription = null) },
                    label = { Text("Post") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Outlined.Checklist, contentDescription = null) },
                    label = { Text("Activity") }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    items(Category.entries, key = { it.name }) { category ->
                        CategoryChip(
                            category = category,
                            selected = category == selectedCategory,
                            onClick = {
                                selectedCategory =
                                    if (selectedCategory == category) null else category
                            }
                        )
                    }
                }
            }

            if (filtered.isEmpty()) {
                item {
                    EmptyState(
                        title = "No listings right now",
                        message = "Nothing matches here yet. Try another category or check back soon."
                    )
                }
            } else {
                item { SectionHeader(title = "Ending soon") }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                        items(endingSoon, key = { it.id }) { listing ->
                            EndingSoonCard(listing, onClick = { onListingClick(listing.id) })
                        }
                    }
                }
                item { SectionHeader(title = "Near you") }
                items(filtered, key = { it.id }) { listing ->
                    ListingCard(listing, onClick = { onListingClick(listing.id) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    FoodRescueTheme  {
        FeedScreen(SampleData.listings, onListingClick = {}, onProfileClick = {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeedScreenDarkPreview() {
    FoodRescueTheme {
        FeedScreen(SampleData.listings, onListingClick = {}, onProfileClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenEmptyPreview() {
    FoodRescueTheme {
        FeedScreen(emptyList(), onListingClick = {}, onProfileClick = {})
    }
}