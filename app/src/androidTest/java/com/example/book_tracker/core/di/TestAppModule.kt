package com.example.book_tracker.core.di

import android.content.Context
import androidx.work.WorkManager
import com.example.book_tracker.core.data.data_source.connection.NetworkConnectivityObserver
import com.example.book_tracker.core.data.data_source.notification.NotificationService
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.repository.ItemRepository
import com.example.book_tracker.core.domain.repository.BookRepository
import com.example.book_tracker.core.domain.repository.UserRepository
import com.example.book_tracker.core.domain.use_case.book.*
import com.example.book_tracker.core.domain.use_case.user.AddUserUseCase
import com.example.book_tracker.core.domain.use_case.user.GetUserUseCase
import com.example.book_tracker.core.domain.use_case.user.GetUserListUseCase
import com.example.book_tracker.core.domain.use_case.user.UserUseCases
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import com.example.book_tracker.core.domain.use_case.book.GetBookListUseCase as GetBookListFromDatabaseUseCase1


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    fun provideNotificationService(@ApplicationContext context: Context): NotificationService =
        NotificationService(context)

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager = WorkManager.getInstance(context)

    @Provides
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(context)

    @Provides
    fun provideSignInClient(@ApplicationContext context: Context): SignInClient = Identity.getSignInClient(context)


//    @Provides
//    fun provideAuthUseCases(authRepository: AuthRepository): AuthUseCases {
//        return AuthUseCases(
//            signInWithCredentialsUseCase = SignInWithCredentialsUseCase(authRepository)
//        )
//    }

    @Provides
    fun provideBookUseCases(
        authRepository: AuthRepository,
        bookRepository: BookRepository,
        itemRepository: ItemRepository
    ): BookUseCases {
        return BookUseCases(
            createBookFromBookUseCase = CreateBookFromBookUseCase(),
            addBookUseCase = AddBookUseCase(bookRepository),
            getItemUseCase = GetItemUseCase(itemRepository),
            getItemListUseCase = GetItemListUseCase(itemRepository),
            getBookUseCase = GetBookUseCase(authRepository, bookRepository),
            getBookListUseCase = GetBookListFromDatabaseUseCase1(
                authRepository = authRepository,
                bookRepository = bookRepository
            )
        )
    }

    @Provides
    fun provideUserUseCases(userRepository: UserRepository): UserUseCases {
        return UserUseCases(
            addUserUseCase = AddUserUseCase(userRepository),
            getUserUseCase = GetUserUseCase(userRepository),
            getUserListUseCase = GetUserListUseCase(userRepository)
        )
    }

}