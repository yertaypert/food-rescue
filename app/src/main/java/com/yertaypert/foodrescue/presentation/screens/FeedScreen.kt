package com.yertaypert.foodrescue.presentation.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.outlined.LocationOff
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
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
    onNotificationsClick: () -> Unit = {},
    locationStatus: LocationStatus = LocationStatus.Pending,
    onEnableLocationClick: () -> Unit = {},
    onRetryLocationClick: () -> Unit = {},
    onLocationServicesClick: () -> Unit = {}
) {
    val selectedCategory = (uiState as? FeedUiState.Success)?.selectedCategory

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (locationStatus is LocationStatus.Denied ||
                                locationStatus is LocationStatus.ServicesOff
                            ) {
                                Icons.Outlined.LocationOff
                            } else {
                                Icons.Outlined.LocationOn
                            },
                            contentDescription = null
                        )
                        Text(
                            text = locationTitle(locationStatus),
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
            when (locationStatus) {
                LocationStatus.Denied -> item(key = "location_banner") {
                    LocationBanner(
                        message = "Location is off — showing all listings.",
                        actionLabel = "Enable",
                        onAction = onEnableLocationClick
                    )
                }

                LocationStatus.ServicesOff -> item(key = "location_banner") {
                    LocationBanner(
                        message = "Location services are off — showing all listings.",
                        actionLabel = "Turn on",
                        onAction = onLocationServicesClick
                    )
                }

                LocationStatus.NoFix -> item(key = "location_banner") {
                    LocationBanner(
                        message = "We couldn't get a location fix — try again, ideally near a window.",
                        actionLabel = "Retry",
                        onAction = onRetryLocationClick
                    )
                }

                else -> Unit
            }

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
    val locationStatus by viewModel.locationStatus.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val locationPermission = Manifest.permission.ACCESS_COARSE_LOCATION

    var askedOnce by rememberSaveable { mutableStateOf(false) }
    var permanentlyDenied by rememberSaveable { mutableStateOf(false) }

    fun isLocationPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(context, locationPermission) ==
            PackageManager.PERMISSION_GRANTED

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    fun openLocationSettings() {
        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        askedOnce = true
        if (granted) {
            permanentlyDenied = false
            viewModel.onLocationPermissionGranted()
        } else {
            // No rationale offered means "don't ask again" was chosen.
            permanentlyDenied = activity?.let {
                !ActivityCompat.shouldShowRequestPermissionRationale(it, locationPermission)
            } ?: true
            viewModel.onLocationPermissionDenied()
        }
    }

    // Ask once, right when the feed (the start destination) first appears.
    LaunchedEffect(Unit) {
        if (isLocationPermissionGranted()) {
            viewModel.onLocationPermissionGranted()
        } else if (!askedOnce) {
            askedOnce = true
            permissionLauncher.launch(locationPermission)
        }
    }

    // Re-check on resume so enabling (or revoking) the permission in Settings is picked up.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (isLocationPermissionGranted()) {
            viewModel.onLocationPermissionGranted()
        } else if (askedOnce) {
            viewModel.onLocationPermissionDenied()
        }
    }

    FeedScreen(
        uiState = uiState,
        onCategorySelected = viewModel::onCategorySelected,
        onFavouriteClick = viewModel::onFavouriteClick,
        onRetry = viewModel::onRetry,
        onListingClick = onListingClick,
        onProfileClick = onProfileClick,
        locationStatus = locationStatus,
        onEnableLocationClick = {
            if (permanentlyDenied) openAppSettings() else permissionLauncher.launch(locationPermission)
        },
        onRetryLocationClick = viewModel::onRetryLocation,
        onLocationServicesClick = { openLocationSettings() }
    )
}


@Composable
private fun LoadingState() {
    Box(Modifier.fillMaxWidth().padding(Spacing.md), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

private fun locationTitle(status: LocationStatus): String = when (status) {
    is LocationStatus.Granted -> status.displayName
    LocationStatus.Pending -> "Nearby"
    LocationStatus.Denied, LocationStatus.ServicesOff -> "Location off"
    LocationStatus.NoFix -> "Location unavailable"
}

@Composable
private fun LocationBanner(
    message: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(
                start = Spacing.md,
                end = Spacing.xs,
                top = Spacing.xs,
                bottom = Spacing.xs
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onAction) { Text(actionLabel) }
        }
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