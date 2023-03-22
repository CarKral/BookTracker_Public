package com.example.book_tracker.core.domain.use_case.user

import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.asResource
import com.example.book_tracker.core.domain.model.user.MyUser
import com.example.book_tracker.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** USE CASE for getting User from Database */
class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(id: String): Flow<Resource<MyUser?>> {
        return userRepository.getUser(id).asResource()
    }
}