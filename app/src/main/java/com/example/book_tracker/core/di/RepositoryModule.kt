package com.example.book_tracker.core.di

import android.content.Context
import com.example.book_tracker.core.data.data_source.network.ItemsApiService
import com.example.book_tracker.core.data.repository.*
import com.example.book_tracker.core.domain.repository.*
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        signInClient: SignInClient,
        firebaseAuth: FirebaseAuth
    ): AuthRepository {
        return AuthRepositoryImpl(context, signInClient, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideItemRepository(api: ItemsApiService): ItemRepository {
        return ItemRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideFirestoreRepository(
        authRepository: AuthRepository,
        firebaseFirestore: FirebaseFirestore,
    ): FirestoreRepository {
        return FirestoreRepositoryImpl(authRepository, firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseStorageRepository(
        storage: StorageReference,
        @ApplicationContext context: Context
    ): StorageRepository {
        return StorageRepositoryImpl(storage, context)
    }

    @Provides
    @Singleton
    fun provideBookRepository(firestoreRepository: FirestoreRepository): BookRepository {
        return BookRepositoryImpl(firestoreRepository)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(firestoreRepository: FirestoreRepository): NoteRepository {
        return NoteRepositoryImpl(firestoreRepository)
    }

    @Provides
    @Singleton
    fun provideReadingRepository(firestoreRepository: FirestoreRepository): ReadingRepository {
        return ReadingRepositoryImpl(firestoreRepository)
    }

    @Provides
    @Singleton
    fun provideUserRepository(firestoreRepository: FirestoreRepository): UserRepository {
        return UserRepositoryImpl(firestoreRepository)
    }

}