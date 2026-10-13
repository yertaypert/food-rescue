package com.yertaypert.foodrescue.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
    uiState: FeedUiState,
    onCategorySelected: (Category) -> Unit,
    onFavouriteClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onListingClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    onNotificationsClick: () -> Unit = {}
) {

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
//                NavigationBarItem(
//                    selected = false,
//                    onClick = {},
//                    icon = { Icon(Icons.Outlined.AddCircleOutline, contentDescription = null) },
//                    label = { Text("Post") }
//                )
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
                            selected = category == uiState.selectedCategory,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }
            }

            when {
                uiState.isLoading -> item { LoadingState() }
                uiState.error != null -> item { ErrorState(uiState.error, onRetry) }
                uiState.listings.isEmpty() -> item {
                    EmptyState(
                        title = "No listings right now",
                        message = "Nothing matches here yet. Try another category or check back soon."
                    )
                }

                else -> {
                    item { SectionHeader(title = "Ending soon") }
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                            items(uiState.endingSoon, key = { it.id }) { listing ->
                                EndingSoonCard(listing, onClick = { onListingClick(listing.id) })
                            }
                        }
                    }
                    item { SectionHeader(title = "Near you") }
                    items(uiState.listings, key = { it.id }) { listing ->
                        ListingCard(
                            listing = listing,
                            isFavourite = listing.id in uiState.favouriteIds,
                            onFavouriteClick = { onFavouriteClick(listing.id) },
                            onClick = { onListingClick(listing.id) }
                        )
                    }
                }
            }
        }
    }
}

// Route: knows the ViewModel
@Composable
fun FeedRoute(
    onListingClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    viewModel: FeedViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FeedScreen(
        uiState = uiState,
        onCategorySelected = viewModel::onCategorySelected,
        onFavouriteClick = viewModel::onFavouriteClick,
        onRetry = viewModel::onRetry,
        onListingClick = onListingClick,
        onProfileClick = onProfileClick
    )
}


@Composable
private fun LoadingState() {
    Box(Modifier.fillMaxWidth().padding(Spacing.md), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        Text(message)
        Button(onClick = onRetry) { Text("Try again") }
    }
}

private fun previewScreenState(state: FeedUiState): @Composable () -> Unit = {
    FoodRescueTheme {
        FeedScreen(state, {}, {}, {}, {}, {})
    }
}

@Preview(showBackground = true) @Composable
private fun FeedLoadingPreview() = previewScreenState(FeedUiState(isLoading = true))()

@Preview(showBackground = true) @Composable
private fun FeedContentPreview() {
    val l = SampleData.listings
    previewScreenState(
        FeedUiState(isLoading = false, listings = l, endingSoon = l.sortedBy { it.minutesLeft }.take(5))
    )()
}

@Preview(showBackground = true) @Composable
private fun FeedEmptyPreview() = previewScreenState(FeedUiState(isLoading = false))()

@Preview(showBackground = true) @Composable
private fun FeedErrorPreview() =
    previewScreenState(FeedUiState(isLoading = false, error = "Could not load listings"))()