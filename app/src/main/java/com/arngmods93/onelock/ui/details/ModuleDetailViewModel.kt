package com.arngmods93.onelock.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arngmods93.onelock.data.model.CompatibilityStatus
import com.arngmods93.onelock.data.model.DeviceSnapshot
import com.arngmods93.onelock.data.model.GoodLockModule
import com.arngmods93.onelock.data.repository.ModuleRepository
import com.arngmods93.onelock.utils.CompatibilityChecker
import com.arngmods93.onelock.utils.DeviceInfo
import com.arngmods93.onelock.utils.PackageUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ModuleDetailUiState(
    val module: GoodLockModule? = null,
    val device: DeviceSnapshot? = null,
    val compatibility: CompatibilityStatus = CompatibilityStatus.UNKNOWN
)

class ModuleDetailViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: ModuleRepository = ModuleRepository()
    private val _uiState = MutableStateFlow(ModuleDetailUiState())
    val uiState: StateFlow<ModuleDetailUiState> = _uiState.asStateFlow()

    fun load(moduleId: String) {
        viewModelScope.launch {
            val rawModule = repository.getModuleById(moduleId)
            val device = DeviceInfo.current()
            
            val module = rawModule?.let {
                val isInstalled = PackageUtils.isModuleInstalled(
                    getApplication<Application>().applicationContext,
                    it.packageName
                )
                it.copy(isInstalled = isInstalled)
            }

            val compatibility = if (module != null) {
                CompatibilityChecker.check(device, module)
            } else {
                CompatibilityStatus.UNKNOWN
            }
            _uiState.update {
                it.copy(module = module, device = device, compatibility = compatibility)
            }
        }
    }
}
