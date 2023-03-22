package com.example.book_tracker.core.di

import android.content.Context
import androidx.work.WorkManager
import com.example.book_tracker.core.data.data_source.connection.NetworkConnectivityObserver
import com.example.book_tracker.core.data.data_source.notification.NotificationService
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.repository.BookRepository
import com.example.book_tracker.core.domain.repository.ItemRepository
import com.example.book_tracker.core.domain.repository.UserRepository
import com.example.book_tracker.core.domain.use_case.auth.AuthUseCases
import com.example.book_tracker.core.domain.use_case.auth.GetAuthUserUseCase
import com.example.book_tracker.core.domain.use_case.auth.SignInWithCredentialsUseCase
import com.example.book_tracker.core.domain.use_case.book.*
import com.example.book_tracker.core.domain.use_case.user.AddUserUseCase
import com.example.book_tracker.core.domain.use_case.user.GetUserListUseCase
import com.example.book_tracker.core.domain.use_case.user.UserUseCases
import com.example.book_tracker.core.domain.util.DefaultDispatchers
import com.example.book_tracker.core.domain.util.DispatcherProvider
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideNotificationService(@ApplicationContext context: Context): NotificationService =
        NotificationService(context)

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context)

    @Provides
    @Singleton
    fun provideSignInClient(@ApplicationContext context: Context): SignInClient = Identity.getSignInClient(context)

    @Provides
    @Singleton
    fun provideDefaultDispatchers(): DispatcherProvider = DefaultDispatchers()

    @Provides
    @Singleton
    fun provideAuthUseCases(authRepository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            signInWithCredentialsUseCase = SignInWithCredentialsUseCase(authRepository),
            getAuthUserUseCase = GetAuthUserUseCase(authRepository)
        )
    }

    @Provides
    @Singleton
    fun provideBookUseCases(
        authRepository: AuthRepository,
        bookRepository: BookRepository,
        itemRepository: ItemRepository
    ): BookUseCases {
        return BookUseCases(
            addBookUseCase = AddBookUseCase(bookRepository),
            getItemUseCase = GetItemUseCase(itemRepository),
            getItemListUseCase = GetItemListUseCase(itemRepository),
            getBookUseCase = GetBookUseCase(authRepository, bookRepository),
            getBookListUseCase = GetBookListUseCase(authRepository, bookRepository)
        )
    }

    @Provides
    @Singleton
    fun provideUserUseCases(userRepository: UserRepository): UserUseCases {
        return UserUseCases(
            addUserUseCase = AddUserUseCase(userRepository),
            getUserUseCase = com.example.book_tracker.core.domain.use_case.user.GetUserUseCase(userRepository),
            getUserListUseCase = GetUserListUseCase(userRepository)
        )
    }
}