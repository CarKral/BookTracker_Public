package com.example.book_tracker.core.di

import com.example.book_tracker.core.data.repository.*
import com.example.book_tracker.core.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object TestRepositoryModule {

    @Provides
    fun provideAuthRepository(
    ): AuthRepository {
        return FakeAuthRepositoryImpl()
    }

    @Provides
    fun provideBookRepository(): ItemRepository {
        return FakeItemRepositoryImpl()
    }

    @Provides
    fun provideFirestoreRepository(
    ): FirestoreRepository {
        return FakeFirestoreRepositoryImpl()
    }

    @Provides
    fun provideFirebaseStorageRepository(): StorageRepository {
        return FakeStorageRepositoryImpl()
    }

    @Provides
    fun provideMyBookRepository(): BookRepository {
        return FakeBookRepositoryImpl()
    }

    @Provides
    fun provideNoteRepository(): NoteRepository {
        return FakeNoteRepositoryImpl()
    }

    @Provides
    fun provideReadingRepository(): ReadingRepository {
        return FakeReadingRepositoryImpl()
    }

    @Provides
    fun provideUserRepository(): UserRepository {
        return FakeUserRepositoryImpl()
    }

}