package com.arngmods93.onelock.data.repository

import com.arngmods93.onelock.data.ModuleCatalog
import com.arngmods93.onelock.data.model.GoodLockModule
import com.arngmods93.onelock.data.model.ModuleCategory

/**
 * Thin repository layer sitting between the static [ModuleCatalog] and the
 * ViewModels. Keeping this indirection means the catalog could later be
 * swapped for a remote/JSON source without touching any UI code.
 */
class ModuleRepository(
    private val allModules: List<GoodLockModule> = ModuleCatalog.modules
) {

    fun getAllModules(): List<GoodLockModule> = allModules

    fun getModuleById(id: String): GoodLockModule? = allModules.firstOrNull { it.id == id }

    fun search(query: String, category: ModuleCategory?, context: android.content.Context? = null): List<GoodLockModule> {
        val trimmedQuery = query.trim()

        return allModules
            .asSequence()
            .filter { module -> category == null || module.category == category }
            .filter { module ->
                if (trimmedQuery.isEmpty()) return@filter true
                module.name.contains(trimmedQuery, ignoreCase = true) ||
                    module.packageName.contains(trimmedQuery, ignoreCase = true) ||
                    module.category.displayName.contains(trimmedQuery, ignoreCase = true)
            }
            .map { module ->
                if (context != null) {
                    module.copy(isInstalled = com.arngmods93.onelock.utils.PackageUtils.isModuleInstalled(context, module.packageName))
                } else {
                    module
                }
            }
            .toList()
    }
}
