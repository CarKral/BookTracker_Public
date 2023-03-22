package com.example.book_tracker.core.di

import android.content.Context
import com.example.book_tracker.core.data.repository.WelcomeStateDataStoreRepositoryImpl
import com.example.book_tracker.core.domain.repository.WelcomeStateDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

//    @Provides
//    fun provideWelcomeStateDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
//        return PreferenceDataStoreFactory.create(
//            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES_NAME)}
//        )
//    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context): WelcomeStateDataStoreRepository = WelcomeStateDataStoreRepositoryImpl(context)

}