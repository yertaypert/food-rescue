package com.yertaypert.foodrescue.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yertaypert.foodrescue.data.Listing
import com.yertaypert.foodrescue.data.SampleData
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme
import com.yertaypert.foodrescue.ui.theme.Spacing
import kotlin.collections.get

@Composable
fun EndingSoonCard(
    listing: Listing,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val urgent = listing.minutesLeft < 10
    OutlinedCard(modifier = modifier.width(180.dp).clickable(onClick = onClick)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(
                    if (urgent) MaterialTheme.colorScheme.errorContainer
                    else listing.category.containerColor()
                )
        ) {
            Surface(
                modifier = Modifier.align(Alignment.TopStart).padding(Spacing.sm),
                shape = MaterialTheme.shapes.small,
                color = if (urgent) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Text(
                    text = "${listing.minutesLeft} min",
                    modifier = Modifier.padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (urgent) MaterialTheme.colorScheme.onError
                    else MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Icon(
                imageVector = listing.category.icon,
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                tint = if (urgent) MaterialTheme.colorScheme.onErrorContainer
                else listing.category.contentColor()
            )
        }
        Column(modifier = Modifier.padding(Spacing.sm)) {
            Text(
                text = listing.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${listing.giverName} · ${listing.distanceKm} km",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun EndingSoonCardPreview() {
    FoodRescueTheme { EndingSoonCard(SampleData.listings[1], onClick = {}) }
}