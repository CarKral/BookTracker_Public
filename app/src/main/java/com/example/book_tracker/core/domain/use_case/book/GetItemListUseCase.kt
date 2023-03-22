package com.example.book_tracker.core.domain.use_case.book

import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.asResource
import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** USE CASE for getting list of Item from Api */
class GetItemListUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(query: String, orderByNewest: Boolean = false): Flow<Resource<List<Item>>> {
        return itemRepository.getItems(
            searchQuery = query,
            limit = 40,
            orderBy = if (orderByNewest) "newest" else "relevance"
        ).asResource()
    }
}