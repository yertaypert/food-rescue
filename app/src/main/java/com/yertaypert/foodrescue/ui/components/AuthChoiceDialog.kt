package com.yertaypert.foodrescue.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yertaypert.foodrescue.ui.theme.FoodRescueTheme
import com.yertaypert.foodrescue.ui.theme.Spacing

@Composable
fun AuthChoiceDialog(
    onDismiss: () -> Unit,
    onRegisterUser: () -> Unit,
    onRegisterGiver: () -> Unit,
    onLogin: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Claim this listing") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text(
                    text = "Create an account or log in to claim food.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(onClick = onRegisterUser, modifier = Modifier.fillMaxWidth()) {
                    Text("Register as user")
                }
                OutlinedButton(onClick = onRegisterGiver, modifier = Modifier.fillMaxWidth()) {
                    Text("Register as giver")
                }
                TextButton(onClick = onLogin, modifier = Modifier.fillMaxWidth()) {
                    Text("I already have an account")
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Not now") } }
    )
}

@Preview
@Composable
private fun AuthChoiceDialogPreview() {
    FoodRescueTheme {
        AuthChoiceDialog(onDismiss = {}, onRegisterUser = {}, onRegisterGiver = {}, onLogin = {})
    }
}