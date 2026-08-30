package com.arngmods93.onelock.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arngmods93.onelock.data.model.DeviceSnapshot
import com.arngmods93.onelock.data.model.ModuleCategory
import com.arngmods93.onelock.data.repository.ModuleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: ModuleRepository = ModuleRepository()
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun setDevice(device: DeviceSnapshot) {
        _uiState.update { it.copy(device = device) }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        refresh()
    }

    fun onCategorySelected(category: ModuleCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val current = _uiState.value
            val results = repository.search(
                current.query,
                current.selectedCategory,
                getApplication<Application>().applicationContext
            )
            _uiState.update { it.copy(modules = results) }
        }
    }
}
