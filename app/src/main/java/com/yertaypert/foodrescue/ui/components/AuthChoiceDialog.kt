package com.yertaypert.foodrescue.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.RamenDining
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme
import com.yertaypert.foodrescue.ui.theme.Spacing

@Composable
fun AuthChoiceDialog(
    onDismiss: () -> Unit,
    onRegisterUser: () -> Unit,
    onRegisterGiver: () -> Unit,
    onLogin: () -> Unit,
    showLoginLink: Boolean = true
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = Spacing.xs
        ) {
            Column(
                modifier = Modifier.padding(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Outlined.Close, contentDescription = "Close")
                }

                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Text(
                        text = "How will you use Food Rescue?",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "This decides what your account can do. " +
                                "You can't switch later without a new account.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                AccountTypeOption(
                    icon = Icons.Outlined.RamenDining,
                    title = "I want to find food",
                    description = "Browse and claim nearby surplus listings",
                    iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onRegisterUser
                )
                AccountTypeOption(
                    icon = Icons.Outlined.Storefront,
                    title = "I have food to give",
                    description = "For registered restaurants, cafes & stores",
                    iconContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    onClick = onRegisterGiver
                )

                if (showLoginLink) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Spacing.sm),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Log in",
                            modifier = Modifier.clickable(onClick = onLogin),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AuthChoiceDialogPreview() {
    FoodRescueTheme {
        AuthChoiceDialog(onDismiss = {}, onRegisterUser = {}, onRegisterGiver = {}, onLogin = {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AuthChoiceDialogDarkPreview() {
    FoodRescueTheme {
        AuthChoiceDialog(onDismiss = {}, onRegisterUser = {}, onRegisterGiver = {}, onLogin = {})
    }
}