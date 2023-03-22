package com.example.book_tracker.core.data.repository

import android.content.Context
import android.content.Intent
import com.example.book_tracker.BuildConfig
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val signInClient: SignInClient,
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    override suspend fun getFirebaseUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    /** Signs in user with given credentials and returns via lambda function if it was successfull */
    override suspend fun signInWithCredentials(
        credential: AuthCredential,
        isSuccess: (Boolean, FirebaseUser?) -> Unit
    ) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                Timber.i("SignInWithCredentials was successful :)")
                isSuccess.invoke(task.isSuccessful, task.result.user)
            }.await()
    }

    /** Signs in user anonymously
     * CURRENTLY NOT USED
     * */
    override suspend fun signInAnonymously() {
        firebaseAuth.signInAnonymously().await()
    }

    /** Signs out user from the app  and returns lambda function for other steps */
    override suspend fun signOut(onSuccess: () -> Unit) {
        if (firebaseAuth.currentUser!!.isAnonymous) {
            firebaseAuth.currentUser!!.delete().await()
        }
        firebaseAuth.signOut()
        signInClient.signOut()
        onSuccess()
        Timber.i("User was signed out.")
    }

    // Sign in - IDENTITIY
    override fun googleIdToken(data: Intent?): String? {
        return signInClient.getSignInCredentialFromIntent(data).googleIdToken
    }

    private val beginSignInRequest =
        BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder().setSupported(true).build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    // Sets whether Google ID token-backed credentials should be returned by the API.
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.GCP_ID)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

    override suspend fun beginSignInResult(): BeginSignInResult {
        return signInClient.beginSignIn(beginSignInRequest).await()
    }

}