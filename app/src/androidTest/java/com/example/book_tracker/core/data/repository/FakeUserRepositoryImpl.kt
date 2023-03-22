package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.model.user.MyUser
import com.example.book_tracker.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class FakeUserRepositoryImpl() : UserRepository {
    override suspend fun getUserList(): Flow<List<MyUser>> {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(myUser: MyUser, onSuccess: () -> Unit, onFailure: () -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(id: String?): Flow<MyUser?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: MyUser?, onSuccess: (user: MyUser?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserName(id: String): String? {
        TODO("Not yet implemented")
    }

}
