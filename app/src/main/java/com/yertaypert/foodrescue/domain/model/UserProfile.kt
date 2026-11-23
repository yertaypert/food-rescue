package com.yertaypert.foodrescue.domain.model

enum class AccountType {
    USER,
    BUSINESS
}

data class UserProfile(
    val uid: String,
    val email: String,
    val displayName: String,
    val accountType: AccountType,
    val businessType: String? = null,
    val address: String? = null
)
