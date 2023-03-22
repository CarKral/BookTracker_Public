package com.example.book_tracker.core.domain.use_case.book

import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.asResource
import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** USE CASE for getting Itemfrom Api */
class GetItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {

    suspend operator fun invoke(bookId: String): Flow<Resource<Item?>> {
        return itemRepository.getItem(bookId).asResource()
    }
}