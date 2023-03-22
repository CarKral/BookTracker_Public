package com.example.book_tracker.core.domain.use_case.auth

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SignInWithCredentialsUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(activityResult: ActivityResult): Flow<Resource<FirebaseUser?>> {
        var resource: Resource<FirebaseUser?> = Resource.Loading

        return flow {
            if (activityResult.resultCode != Activity.RESULT_OK) {
                // The user cancelled the login, was it due to an Exception?
                if (activityResult.data?.action == ActivityResultContracts.StartIntentSenderForResult.ACTION_INTENT_SENDER_REQUEST) {
                    val exception = activityResult.data?.getStringExtra(
                        ActivityResultContracts.StartIntentSenderForResult.EXTRA_SEND_INTENT_EXCEPTION
                    )
                    Timber.e("Couldn't start One Tap UI: ${exception.toString()}")
                }
                resource =
                    Resource.Error(Throwable("Given activity result is not RESULT_OK (RESULT CODE: ${activityResult.resultCode}"))
            }

            val idToken = authRepository.googleIdToken(activityResult.data)

            if (idToken != null) {
                Timber.i("Google Id Token is: $idToken")

                val credential = GoogleAuthProvider.getCredential(idToken, null)
                authRepository.signInWithCredentials(
                    credential = credential,
                    isSuccess = { isSuccess, user ->
                        resource = if (isSuccess) Resource.Success(user)
                        else Resource.Error(Throwable("Something wrong with sign in."))
                    })
            } else {
                resource = Resource.Error(Throwable("GoogleIdToken is null."))
            }

            when (resource) {
                is Resource.Loading -> Timber.d("RESOURCE AUTH: LOADING")
                is Resource.Success -> Timber.d("RESOURCE AUTH: SUCCESS")
                is Resource.Error -> Timber.e("RESOURCE AUTH: ERROR -> " + (resource as Resource.Error).exception.toString())
            }

            emit(resource)
        }
    }
}