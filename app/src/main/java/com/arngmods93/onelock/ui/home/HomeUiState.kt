package com.arngmods93.onelock.ui.home

import com.arngmods93.onelock.data.model.DeviceSnapshot
import com.arngmods93.onelock.data.model.GoodLockModule
import com.arngmods93.onelock.data.model.ModuleCategory

data class HomeUiState(
    val device: DeviceSnapshot? = null,
    val query: String = "",
    val selectedCategory: ModuleCategory? = null,
    val modules: List<GoodLockModule> = emptyList()
)
