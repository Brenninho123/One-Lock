package com.arngmods93.onelock.data.model

/**
 * Represents a single Samsung Good Lock module in the One Lock catalog.
 */
data class GoodLockModule(
    val id: String,
    val name: String,
    val packageName: String,
    val description: String,
    val category: ModuleCategory,
    val apkMirrorUrl: String,
    val minimumAndroid: String,
    val minimumAndroidSdk: Int,
    val supportedOneUI: String,
    val isInstalled: Boolean = false
)
