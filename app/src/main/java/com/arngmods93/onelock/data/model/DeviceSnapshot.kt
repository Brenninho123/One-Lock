package com.arngmods93.onelock.data.model

/**
 * Snapshot of the current device, detected without ADB or root access.
 * [oneUiVersion] is best-effort: Samsung does not expose a public,
 * guaranteed API for this, so it can legitimately be null on some
 * devices, ROMs, or non-Samsung manufacturers.
 */
data class DeviceSnapshot(
    val manufacturer: String,
    val model: String,
    val androidVersionName: String,
    val androidSdkInt: Int,
    val oneUiVersion: String?
) {
    val isSamsung: Boolean
        get() = manufacturer.contains("samsung", ignoreCase = true)
}
