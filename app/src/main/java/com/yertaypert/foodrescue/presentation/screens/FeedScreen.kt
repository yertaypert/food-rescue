package com.yertaypert.foodrescue.presentation.screens

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yertaypert.foodrescue.domain.model.Category
import com.yertaypert.foodrescue.presentation.components.CategoryChip
import com.yertaypert.foodrescue.presentation.components.EmptyState
import com.yertaypert.foodrescue.presentation.components.EndingSoonCard
import com.yertaypert.foodrescue.presentation.components.SectionHeader
import com.yertaypert.foodrescue.presentation.theme.Spacing
import com.yertaypert.foodrescue.data.local.SampleData
import com.yertaypert.foodrescue.presentation.components.ListingCard
import com.yertaypert.foodrescue.presentation.theme.FoodRescueTheme

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
    val selectedCategory = (uiState as? FeedUiState.Success)?.selectedCategory

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
                            selected = category == selectedCategory,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }
            }

            when (val state = uiState) {
                FeedUiState.Loading -> item { LoadingState() }
                is FeedUiState.Error -> item { ErrorState(state.message, onRetry) }
                is FeedUiState.Success -> if (state.listings.isEmpty()) {
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
                            items(state.endingSoon, key = { it.id }) { listing ->
                                EndingSoonCard(listing, onClick = { onListingClick(listing.id) })
                            }
                        }
                    }
                    item { SectionHeader(title = "Near you") }
                    items(state.listings, key = { it.id }) { listing ->
                        ListingCard(
                            listing = listing,
                            isFavourite = listing.id in state.favouriteIds,
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
    viewModel: FeedViewModel = hiltViewModel()
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
private fun FeedLoadingPreview() = previewScreenState(FeedUiState.Loading)()

@Preview(showBackground = true) @Composable
private fun FeedContentPreview() {
    val l = SampleData.listings
    previewScreenState(
        FeedUiState.Success(
            listings = l,
            endingSoon = l.sortedBy { it.minutesLeft }.take(5),
            selectedCategory = null,
            favouriteIds = emptySet()
        )
    )()
}

@Preview(showBackground = true) @Composable
private fun FeedEmptyPreview() = previewScreenState(
    FeedUiState.Success(
        listings = emptyList(),
        endingSoon = emptyList(),
        selectedCategory = null,
        favouriteIds = emptySet()
    )
)()

@Preview(showBackground = true) @Composable
private fun FeedErrorPreview() =
    previewScreenState(FeedUiState.Error("Could not load listings"))()