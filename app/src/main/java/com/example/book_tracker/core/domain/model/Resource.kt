package com.example.book_tracker.core.domain.model

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException


/**
 *
 * */
sealed interface Resource<out T> {
    object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val exception: Throwable?) : Resource<Nothing>
}

/**
 *
 * */
fun <T> Flow<T>.asResource(): Flow<Resource<T>> {
    return this.map<T, Resource<T>> {
        Timber.d("RESOURCE: SUCCESS -> ${it.toString()}")
        Resource.Success(it)
    }
        .onStart {
            Timber.d("RESOURCE: LOADING")
            emit(Resource.Loading) }
        .retryWhen { cause, attempt ->
            if (cause is IOException && attempt < 3) {
                delay(5000L)
                true
            } else false
        }
        .catch {
            Timber.e("RESOURCE: ERROR")
            emit(Resource.Error(it))
        }
}


