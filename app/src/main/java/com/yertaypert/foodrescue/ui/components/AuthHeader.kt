package com.yertaypert.foodrescue.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme
import com.yertaypert.foodrescue.ui.theme.Spacing

@Composable
fun AuthHeader(
    icon: ImageVector,
    subtitle: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = if (horizontalAlignment == Alignment.CenterHorizontally) {
                TextAlign.Center
            } else {
                TextAlign.Start
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthHeaderPreview() {
    FoodRescueTheme {
        AuthHeader(icon = Icons.Outlined.Eco, subtitle = "Log in to claim or manage listings")
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AuthHeaderDarkPreview() {
    FoodRescueTheme {
        AuthHeader(icon = Icons.Outlined.Eco, subtitle = "Log in to claim or manage listings")
    }
}