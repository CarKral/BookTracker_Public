package com.example.book_tracker.features.feature_settings.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SettingsState(
    val isLoading: Boolean = false,
)

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
) : ViewModel() {


    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()


}