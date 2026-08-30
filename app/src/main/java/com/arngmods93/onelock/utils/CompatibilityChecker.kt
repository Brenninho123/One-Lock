package com.arngmods93.onelock.utils

import com.arngmods93.onelock.data.model.CompatibilityStatus
import com.arngmods93.onelock.data.model.DeviceSnapshot
import com.arngmods93.onelock.data.model.GoodLockModule

/**
 * Purely informative compatibility comparison between a [DeviceSnapshot]
 * and a [GoodLockModule]. This never blocks, hides, or alters any action
 * in the app — it only decides which badge (🟢/🟡/🔴) to show.
 */
object CompatibilityChecker {

    fun check(device: DeviceSnapshot, module: GoodLockModule): CompatibilityStatus {
        // Good Lock modules are Samsung-only. On a non-Samsung device we
        // simply don't know how the module will behave.
        if (!device.isSamsung) return CompatibilityStatus.UNKNOWN

        return when {
            device.androidSdkInt >= module.minimumAndroidSdk -> CompatibilityStatus.COMPATIBLE
            device.androidSdkInt < module.minimumAndroidSdk - 2 -> CompatibilityStatus.POSSIBLY_INCOMPATIBLE
            else -> CompatibilityStatus.UNKNOWN
        }
    }
}
