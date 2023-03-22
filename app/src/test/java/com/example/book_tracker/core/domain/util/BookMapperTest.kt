package com.example.book_tracker.core.domain.util

import com.example.book_tracker.core.domain.model.book.Book
import com.example.book_tracker.core.domain.model.book.BookPrintType
import com.example.book_tracker.core.domain.model.book.BookShelf
import com.example.book_tracker.core.domain.model.book.Item
import com.example.book_tracker.core.domain.model.fakeBook
import com.example.book_tracker.core.domain.model.fakeItem
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BookMapperTest {
    private lateinit var book: Book
    private lateinit var item: Item
    private lateinit var createBookFromBookUseCase: CreateBookFromBookUseCase

    @Before
    fun setUp() {
        book = fakeBook
        item = fakeItem
        createBookFromBookUseCase = CreateBookFromBookUseCase()
    }

    @Test
    fun `Create new instance MyBook from given Item, Assert that book is not null and fields are equals`() {
        val newItem = item.mapToMewBook(null)

        assertThat(newItem).isNotNull()
        assertThat(newItem).isInstanceOf(Book::class.java)
        assertThat(newItem.title == item.volumeInfo?.title).isTrue()
        assertThat(newItem.subtitle == item.volumeInfo?.subtitle).isTrue()
        assertThat(newItem.authors == item.volumeInfo?.authors?.joinToString()).isTrue()
        assertThat(newItem.categories == item.volumeInfo?.categories?.joinToString()).isTrue()
        assertThat(newItem.publisher == item.volumeInfo?.publisher).isTrue()
        assertThat(newItem.pageCount == item.volumeInfo?.pageCount).isTrue()
        assertThat(newItem.googleBookId == item.id).isTrue()
        assertThat(newItem.bookShelf == BookShelf.Unclassified.toString()).isTrue()
        assertThat(newItem.printType?.get(0) == BookPrintType.Book.toString()).isTrue()
    }

    @Test
    fun `Create new instance of MyBook from given MyBook, Assert that book is not null and fields are equals`() {
        val newBook = book.mapToMewBook(null)

        assertThat(newBook).isNotNull()
        assertThat(newBook).isInstanceOf(Book::class.java)
        assertThat(newBook.title == book.title).isTrue()
        assertThat(newBook.subtitle == book.subtitle).isTrue()
        assertThat(newBook.authors == book.authors).isTrue()
        assertThat(newBook.categories == book.categories).isTrue()
        assertThat(newBook.publisher == book.publisher).isTrue()
        assertThat(newBook.pageCount == book.pageCount).isTrue()
        assertThat(newBook.bookShelf == BookShelf.Unclassified.toString()).isTrue()
        assertThat(newBook.printType?.get(0) == BookPrintType.Book.toString()).isTrue()

    }

}