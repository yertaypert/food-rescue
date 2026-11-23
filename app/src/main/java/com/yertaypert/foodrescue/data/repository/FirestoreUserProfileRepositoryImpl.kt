package com.yertaypert.foodrescue.data.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.yertaypert.foodrescue.domain.model.AccountType
import com.yertaypert.foodrescue.domain.model.UserProfile
import com.yertaypert.foodrescue.domain.repository.UserProfileRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "FirestoreProfileRepository"
private const val USERS_COLLECTION = "users"
private const val FIELD_EMAIL = "email"
private const val FIELD_DISPLAY_NAME = "displayName"
private const val FIELD_ACCOUNT_TYPE = "accountType"
private const val FIELD_BUSINESS_TYPE = "businessType"
private const val FIELD_ADDRESS = "address"
private const val FIELD_CREATED_AT = "createdAt"

@Singleton
class FirestoreUserProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserProfileRepository {

    override suspend fun saveProfile(profile: UserProfile): Boolean {
        return try {
            firestore.collection(USERS_COLLECTION)
                .document(profile.uid)
                .set(profile.toDocument())
                .await()
            true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w(TAG, "Could not save profile for ${profile.uid}", e)
            false
        }
    }

    private fun UserProfile.toDocument(): Map<String, Any?> = mapOf(
        FIELD_EMAIL to email,
        FIELD_DISPLAY_NAME to displayName,
        FIELD_ACCOUNT_TYPE to when (accountType) {
            AccountType.USER -> "user"
            AccountType.BUSINESS -> "business"
        },
        FIELD_BUSINESS_TYPE to businessType,
        FIELD_ADDRESS to address,
        FIELD_CREATED_AT to FieldValue.serverTimestamp()
    )
}
