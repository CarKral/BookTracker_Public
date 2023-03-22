package com.example.book_tracker.core.data.data_source.network

import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.domain.model.book.ItemsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ItemsApiService {

//    GET https://www.googleapis.com/books/v1/volumes?q={search terms}

    //    @GET("volumes?+intitle&maxResults=40&orderBy=relevance")
//    @GET("volumes?&maxResults=40&filter=partial&orderBy=relevance")
    @GET("volumes?&maxResults=40&orderBy=relevance")
    suspend fun getAllItems(@Query("q") query: String): ItemsResponse

    @GET("volumes")
    suspend fun getItems(
        @Query("q") query: String,
        @Query("maxResults") maxResult: Int? = 40,
        @Query("orderBy") orderBy: String? = "relevance",
    ): ItemsResponse

    @GET("volumes?&maxResults=40&orderBy=newest")
    suspend fun getAllItemsByNewest(@Query("q") query: String): ItemsResponse

    @GET("volumes/{id}")
    suspend fun getItem(@Path("id") id: String): Item

}