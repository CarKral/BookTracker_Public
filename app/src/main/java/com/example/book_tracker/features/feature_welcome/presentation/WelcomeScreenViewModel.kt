package com.example.book_tracker.features.feature_welcome.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.book_tracker.core.domain.repository.WelcomeStateDataStoreRepository
import com.example.book_tracker.core.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    private val dataStoreRepository: WelcomeStateDataStoreRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    fun saveWelcomeState(completed: Boolean) {
        viewModelScope.launch(dispatchers.io) {
            dataStoreRepository.saveWelcomeState(completed)
        }
    }
}