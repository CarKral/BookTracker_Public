package com.example.book_tracker.core.domain.repository

import com.example.book_tracker.core.domain.model.user.MyUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserList(): Flow<List<MyUser>>

    suspend fun addUser(myUser: MyUser, onSuccess: () -> Unit, onFailure: () -> Unit)
    suspend fun getUser(id: String?): Flow<MyUser?>
    suspend fun updateUser(user: MyUser?, onSuccess: (user: MyUser?) -> Unit)

    suspend fun getUserName(id: String): String?
}