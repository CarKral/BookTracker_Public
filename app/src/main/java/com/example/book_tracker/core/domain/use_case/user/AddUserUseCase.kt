package com.example.book_tracker.core.domain.use_case.user

import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.user.MyUser
import com.example.book_tracker.core.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/** USE CASE for adding User to Database */
class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(firebaseUser: FirebaseUser?): Flow<Resource<MyUser?>> {
        return if (firebaseUser != null) {
            flow {
                var resource: Resource<MyUser> = Resource.Loading
                val myUser = MyUser()

                userRepository.addUser(
                    myUser.copy(
                        id = firebaseUser.uid,
                        name = firebaseUser.displayName,
                        photo = firebaseUser.photoUrl.toString()

                    ),
                    onSuccess = {
                        resource = Resource.Success(myUser)
                    },
                    onFailure = {
                        resource = Resource.Error(Throwable("Failure from userRepository"))
                    }
                )
                emit(resource)

            }
        } else {
            flow { emit(Resource.Error(Throwable("Firebase user is null"))) }
        }
    }
}