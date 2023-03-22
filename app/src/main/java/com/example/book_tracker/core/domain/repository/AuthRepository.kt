package com.example.book_tracker.core.domain.repository

import android.content.Intent
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    val currentUserId: String?
    suspend fun getFirebaseUser(): FirebaseUser?

    suspend fun signInWithCredentials(credential: AuthCredential, isSuccess: (Boolean, FirebaseUser?) -> Unit)
    suspend fun signInAnonymously()
    suspend fun signOut(onSuccess: () -> Unit)

//    val signInClient: SignInClient
    fun googleIdToken(data: Intent?): String?
    suspend fun beginSignInResult(): BeginSignInResult
}