package com.example.book_tracker.core.domain.use_case.book

import com.example.book_tracker.core.di.RepositoryModule
import com.example.book_tracker.core.domain.model.Resource
import com.example.book_tracker.core.domain.model.fakeBook
import com.example.book_tracker.core.domain.repository.AuthRepository
import com.example.book_tracker.core.domain.repository.BookRepository
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class GetBookUseCaseTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var bookRepository: BookRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun getBookFromDatabase_isNotNull() {
        runBlocking {
            val getBookUseCase = GetBookUseCase(authRepository, bookRepository)
            getBookUseCase(userId = "HjDeyR9rngVTv9YZkXfiNz5SHwB3", bookId = "eLaRDs92AnCoHtv9efrK").collect {
                when (it) {
                    is Resource.Success -> {
                        println(it.data.toString())
                        assertThat(it.data == fakeBook).isTrue()
                        assertThat(it).isInstanceOf(Resource.Success::class.java)
                    }
                    is Resource.Error -> {
                        println(it.exception?.message.toString())
                        assertThat(it.exception).isInstanceOf(Exception::class.java)
                    }
                    is Resource.Loading -> {
                        println(it.toString())
                        assertThat(it).isInstanceOf(Resource.Loading::class.java)
                    }
                }
            }
        }
    }
}