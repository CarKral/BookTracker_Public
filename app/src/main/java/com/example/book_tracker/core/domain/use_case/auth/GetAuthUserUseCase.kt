package com.example.book_tracker.core.domain.use_case.auth

import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAuthUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Flow<Resource<FirebaseUser?>> {
        flow { emit(Resource.Loading) }
        return try {
            flow { emit(Resource.Success(authRepository.getFirebaseUser())) }
        } catch (e: Exception) {
            flow { emit(Resource.Error(e)) }
        }
    }
}