package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow

class FakeItemRepositoryImpl() : ItemRepository {
    override suspend fun getItems(searchQuery: String): Flow<List<Item>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNewestItems(searchQuery: String): Flow<List<Item>> {
        TODO("Not yet implemented")
    }

    override suspend fun getItem(bookId: String): Flow<Item> {
        TODO("Not yet implemented")
    }

    override fun addBook() {
        TODO("Not yet implemented")
    }

    override fun updateBook() {
        TODO("Not yet implemented")
    }

    override fun deleteBook() {
        TODO("Not yet implemented")
    }

}
