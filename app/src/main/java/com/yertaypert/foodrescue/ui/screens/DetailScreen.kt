package com.yertaypert.foodrescue.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yertaypert.foodrescue.data.Listing
import com.yertaypert.foodrescue.data.SampleData
import com.yertaypert.foodrescue.ui.components.AuthChoiceDialog
import com.yertaypert.foodrescue.ui.components.EmptyState
import com.yertaypert.foodrescue.ui.components.InfoTile
import com.yertaypert.foodrescue.ui.components.containerColor
import com.yertaypert.foodrescue.ui.components.contentColor
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme
import com.yertaypert.foodrescue.ui.theme.Spacing

private fun Listing.endsInText(): String =
    if (minutesLeft < 60) "Ends in $minutesLeft min"
    else "Ends in ${minutesLeft / 60} h"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    listingId: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onRegisterUser: () -> Unit = {},
    onRegisterGiver: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    // The screen receives only an id and finds the item itself.
    val listing = remember(listingId) { SampleData.byId(listingId) }

    var isFavourite by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }

    val heroColor = listing?.category?.containerColor() ?: MaterialTheme.colorScheme.surface

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = listing?.title ?: "Listing",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (listing != null) {
                        IconButton(onClick = { isFavourite = !isFavourite }) {
                            Icon(
                                imageVector = if (isFavourite) Icons.Filled.Favorite
                                else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (isFavourite) "Remove from favourites"
                                else "Add to favourites"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = heroColor)
            )
        },
        bottomBar = {
            if (listing != null) {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    Column(modifier = Modifier.navigationBarsPadding()) {
                        HorizontalDivider()
                        Button(
                            onClick = { showAuthDialog = true },
                            modifier = Modifier.fillMaxWidth().padding(Spacing.md)
                        ) {
                            Text("Claim this listing")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (listing == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    title = "Listing not found",
                    message = "It may have been claimed or removed."
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Hero
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(heroColor)
                ) {
                    Image(
                        painter = painterResource(listing.imageRes),
                        contentDescription = "${listing.category.label} listing: ${listing.title}",
                        modifier = Modifier.align(Alignment.Center).size(96.dp),
                        colorFilter = ColorFilter.tint(listing.category.contentColor())
                    )
                    Surface(
                        modifier = Modifier.align(Alignment.BottomStart).padding(Spacing.md),
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Text(
                            text = listing.endsInText(),
                            modifier = Modifier.padding(
                                horizontal = Spacing.sm,
                                vertical = Spacing.xs
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md)
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = listing.category.label,
                            modifier = Modifier.padding(
                                horizontal = Spacing.sm,
                                vertical = Spacing.xs
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Column {
                        Text(
                            text = listing.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = listing.giverName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
                        InfoTile(
                            icon = Icons.Outlined.Schedule,
                            title = listing.pickupWindow,
                            subtitle = "Pickup window",
                            modifier = Modifier.weight(1f)
                        )
                        InfoTile(
                            icon = Icons.Outlined.LocationOn,
                            title = "${listing.distanceKm} km away",
                            subtitle = listing.area,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = listing.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(Spacing.md),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Exact pickup address is revealed after you claim this listing.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAuthDialog) {
        AuthChoiceDialog(
            onDismiss = { showAuthDialog = false },
            onRegisterUser = { showAuthDialog = false; onRegisterUser() },
            onRegisterGiver = { showAuthDialog = false; onRegisterGiver() },
            onLogin = { showAuthDialog = false; onLogin() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    FoodRescueTheme { DetailScreen(listingId = 1, onBack = {}) }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailScreenDarkPreview() {
    FoodRescueTheme { DetailScreen(listingId = 1, onBack = {}) }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenNotFoundPreview() {
    FoodRescueTheme { DetailScreen(listingId = 999, onBack = {}) }
}