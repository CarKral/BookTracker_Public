package com.example.book_tracker.core.domain.repository

import com.example.book_tracker.core.domain.model.book.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    suspend fun getItems(searchQuery: String, limit: Int?, orderBy: String?): Flow<List<Item>>
    suspend fun getNewestItems(searchQuery: String): Flow<List<Item>>
    suspend fun getItem(id: String): Flow<Item>
//    suspend fun getBookISBN(isbn: String): Resources<List<Item>>

}
