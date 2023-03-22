package com.example.book_tracker.core.di

import com.example.book_tracker.core.data.data_source.network.ItemsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BooksApiModule {

    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    @Singleton
    @Provides
    fun provideBooksApi(): ItemsApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItemsApiService::class.java)
    }
}