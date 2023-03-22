package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.model.user.MyUser
import com.example.book_tracker.core.domain.repository.FirestoreRepository
import com.example.book_tracker.core.domain.repository.UserRepository
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    firestoreRepository: FirestoreRepository
) : UserRepository {

    private val usersCollection = firestoreRepository.usersCollection

    override suspend fun getUserList(): Flow<List<MyUser>> {
        return usersCollection.snapshots()
            .map { value: QuerySnapshot -> value.toObjects(MyUser::class.java) }
    }

    override suspend fun addUser(myUser: MyUser, onSuccess: () -> Unit, onFailure: () -> Unit) {
        myUser.id?.let {
            usersCollection.document(it).set(myUser).addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onFailure()
            }.await()
        }
    }

    override suspend fun getUser(id: String?): Flow<MyUser?> {
        return if (id != null) usersCollection.document(id).snapshots().map { value ->
            Timber.d("USER" + value.data.toString())
            value.toObject(MyUser::class.java)
        } else flow { emit(null) }
    }

    override suspend fun updateUser(user: MyUser?, onSuccess: (user: MyUser?) -> Unit) {
        user?.id?.let {
            usersCollection.document(it).set(user).addOnSuccessListener {
                onSuccess(user)
            }.await()
        }
    }

    override suspend fun getUserName(id: String): String? {
        var userName: String? = null
        usersCollection.document(id).get().addOnSuccessListener { value ->
            userName = value.toObject(MyUser::class.java)?.name
        }
        return userName
    }

}