package com.yertaypert.foodrescue.presentation.screens

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yertaypert.foodrescue.R
import com.yertaypert.foodrescue.data.local.SampleData
import com.yertaypert.foodrescue.domain.model.Listing
import com.yertaypert.foodrescue.presentation.components.AuthChoiceDialog
import com.yertaypert.foodrescue.presentation.components.EmptyState
import com.yertaypert.foodrescue.presentation.components.InfoTile
import com.yertaypert.foodrescue.presentation.components.containerColor
import com.yertaypert.foodrescue.presentation.components.contentColor
import com.yertaypert.foodrescue.presentation.theme.FoodRescueTheme
import com.yertaypert.foodrescue.presentation.theme.Spacing

private fun Listing.endsInText(): String =
    if (minutesLeft < 60) "Ends in $minutesLeft min"
    else "Ends in ${minutesLeft / 60} h"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onBack: () -> Unit,
    onFavouriteClick: () -> Unit,
    onClaimClick: () -> Unit,
    onDismissAuthDialog: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onRegisterUser: () -> Unit = {},
    onRegisterGiver: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    val success = uiState as? DetailUiState.Success
    val listing = success?.listing
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
                        IconButton(onClick = onFavouriteClick) {
                            Icon(
                                imageVector = if (success?.isFavourite == true) Icons.Filled.Favorite
                                else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (success?.isFavourite == true) "Remove from favourites"
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
                            onClick = onClaimClick,
                            modifier = Modifier.fillMaxWidth().padding(Spacing.md)
                        ) {
                            Text("Claim this listing")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        when (val state = uiState) {
            DetailUiState.Loading -> DetailLoading(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )

            is DetailUiState.Error -> DetailError(
                message = state.message,
                onRetry = onRetry,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )

            DetailUiState.NotFound -> Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    title = "Listing not found",
                    message = "It may have been claimed or removed."
                )
            }

            is DetailUiState.Success -> {
                val listing = state.listing
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
                        painter = painterResource(R.drawable.ic_food_hero),
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
                            title = "${listing.formattedDistance} away",
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
    }

    if (success?.isAuthDialogVisible == true) {
        AuthChoiceDialog(
            onDismiss = onDismissAuthDialog,
            onRegisterUser = { onDismissAuthDialog(); onRegisterUser() },
            onRegisterGiver = { onDismissAuthDialog(); onRegisterGiver() },
            onLogin = { onDismissAuthDialog(); onLogin() }
        )
    }
}

@Composable
fun DetailRoute(
    onBack: () -> Unit,
    onRegisterUser: () -> Unit,
    onRegisterGiver: () -> Unit,
    onLogin: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetailScreen(
        uiState = uiState,
        onBack = onBack,
        onFavouriteClick = viewModel::onFavouriteClick,
        onClaimClick = viewModel::onClaimClick,
        onDismissAuthDialog = viewModel::onAuthDialogDismiss,
        onRetry = viewModel::onRetry,
        onRegisterUser = onRegisterUser,
        onRegisterGiver = onRegisterGiver,
        onLogin = onLogin
    )
}

@Composable
private fun DetailLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DetailError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message)
        Button(onClick = onRetry) { Text("Try again") }
    }
}

@Composable
private fun DetailPreviewContent(uiState: DetailUiState) {
    FoodRescueTheme {
        DetailScreen(
            uiState = uiState,
            onBack = {},
            onFavouriteClick = {},
            onClaimClick = {},
            onDismissAuthDialog = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    DetailPreviewContent(
        DetailUiState.Success(SampleData.byId(1)!!)
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DetailScreenDarkPreview() {
    DetailPreviewContent(
        DetailUiState.Success(SampleData.byId(1)!!, isFavourite = true)
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenLoadingPreview() {
    DetailPreviewContent(DetailUiState.Loading)
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenErrorPreview() {
    DetailPreviewContent(DetailUiState.Error("Could not load listing"))
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenNotFoundPreview() {
    DetailPreviewContent(DetailUiState.NotFound)
}
