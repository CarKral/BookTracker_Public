package com.example.book_tracker.core.data.repository

import com.example.book_tracker.core.data.data_source.network.ItemsApiService
import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(private val api: ItemsApiService) : ItemRepository {

    override suspend fun getItems(searchQuery: String, limit: Int?, orderBy: String?): Flow<List<Item>> {
        return flow {
//            val items = api.getAllItems(searchQuery).items
            val items = api.getItems(searchQuery, limit, orderBy).items
            emit(items)
        }
    }

    override suspend fun getNewestItems(searchQuery: String): Flow<List<Item>> {
        return flow {
            val items = api.getAllItemsByNewest(searchQuery).items
            emit(items)
        }
    }

    override suspend fun getItem(id: String): Flow<Item> {
        return flow {
            val item = api.getItem(id)
            Timber.e("Item from api - " + item.toString())

            emit(item)
        }
    }
}
