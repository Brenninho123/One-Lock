package com.arngmods93.onelock.ui.navigation

/**
 * Central place for every navigation destination in One Lock, so routes
 * are never hardcoded as raw strings scattered across composables.
 */
object OneLockDestinations {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val MODULE_DETAIL = "module/{moduleId}"

    fun moduleDetail(moduleId: String) = "module/$moduleId"

    const val MODULE_ID_ARG = "moduleId"
}
