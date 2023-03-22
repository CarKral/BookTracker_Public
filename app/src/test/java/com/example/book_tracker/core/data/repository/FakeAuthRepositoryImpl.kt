package com.example.book_tracker.core.data.repository

import android.content.Intent
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

class FakeAuthRepositoryImpl() : AuthRepository {

    override val currentUserId: String?
        get() = ""

    override suspend fun getFirebaseUser(user: (FirebaseUser?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun signInWithCredentials(
        credential: AuthCredential,
        isSuccess: (Boolean, FirebaseUser?) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun signInAnonymously() {
        TODO("Not yet implemented")
    }

    override suspend fun signOut(onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun googleIdToken(data: Intent?): String? {
        TODO("Not yet implemented")
    }

    override suspend fun beginSignInResult(): BeginSignInResult {
        TODO("Not yet implemented")
    }

}
