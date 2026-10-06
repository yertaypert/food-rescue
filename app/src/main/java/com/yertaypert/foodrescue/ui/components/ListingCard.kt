package com.yertaypert.foodrescue.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow
import com.yertaypert.foodrescue.data.Listing
import com.yertaypert.foodrescue.data.SampleData
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme
import com.yertaypert.foodrescue.ui.theme.Spacing
import kotlin.collections.get

@Composable
fun ListingCard(
    listing: Listing,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(Spacing.md),
            horizontalArrangement = Arrangement.spacedBy(Spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(category = listing.category)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = listing.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = listing.giverName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "${listing.distanceKm} km",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
private fun ListingCardPreview() {
    FoodRescueTheme { ListingCard(SampleData.listings[4], onClick = {}) }
}