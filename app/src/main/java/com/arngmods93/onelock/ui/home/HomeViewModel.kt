package com.arngmods93.onelock.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arngmods93.onelock.data.model.DeviceSnapshot
import com.arngmods93.onelock.data.model.ModuleCategory
import com.arngmods93.onelock.data.repository.ModuleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: ModuleRepository = ModuleRepository()

    private val _query = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow<ModuleCategory?>(null)
    private val _device = MutableStateFlow<DeviceSnapshot?>(null)
    private val _refreshSignal = MutableSharedFlow<Unit>(replay = 1)

    private val searchResults = combine(
        _query.debounce(250L).distinctUntilChanged(),
        _selectedCategory.distinctUntilChanged(),
        _refreshSignal.onStart { emit(Unit) }
    ) { query, category, _ -> query to category }
        .flatMapLatest { (query, category) ->
            flow {
                emit(HomeSearchResult.Loading)
                try {
                    val results = repository.search(
                        query,
                        category,
                        getApplication<Application>().applicationContext
                    )
                    emit(HomeSearchResult.Success(results))
                } catch (e: Exception) {
                    emit(HomeSearchResult.Error(e.message ?: "Unknown error"))
                }
            }
        }
        .catch { emit(HomeSearchResult.Error(it.message ?: "Unknown error")) }

    val uiState: StateFlow<HomeUiState> = combine(
        _query,
        _selectedCategory,
        _device,
        searchResults
    ) { query, category, device, result ->
        HomeUiState(
            query = query,
            selectedCategory = category,
            device = device,
            modules = (result as? HomeSearchResult.Success)?.modules.orEmpty(),
            isLoading = result is HomeSearchResult.Loading,
            errorMessage = (result as? HomeSearchResult.Error)?.message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState(isLoading = true)
    )

    fun setDevice(device: DeviceSnapshot) {
        _device.update { device }
    }

    fun onQueryChange(query: String) {
        _query.update { query }
    }

    fun onCategorySelected(category: ModuleCategory?) {
        _selectedCategory.update { category }
    }

    fun refresh() {
        viewModelScope.launch {
            _refreshSignal.emit(Unit)
        }
    }
}

private sealed interface HomeSearchResult {
    data object Loading : HomeSearchResult
    data class Success(val modules: List<com.arngmods93.onelock.data.model.GoodLockModule>) : HomeSearchResult
    data class Error(val message: String) : HomeSearchResult
}
