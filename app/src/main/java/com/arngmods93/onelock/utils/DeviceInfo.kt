package com.arngmods93.onelock.utils

import android.os.Build
import com.arngmods93.onelock.data.model.DeviceSnapshot

/**
 * Detects basic device information without requiring root or ADB.
 *
 * One UI version detection is best-effort: Samsung does not offer an
 * official, documented public API for this. We read the system property
 * `ro.build.version.oneui` via reflection into [android.os.Build] class
 * loader — this is safe (read-only, no root) but may legitimately return
 * null on non-Samsung devices, some carrier ROMs, or after platform
 * changes that remove/rename the property. Any failure is swallowed and
 * simply reported as "unknown" to the user.
 */
object DeviceInfo {

    fun current(): DeviceSnapshot {
        return DeviceSnapshot(
            manufacturer = Build.MANUFACTURER.orEmpty().ifBlank { "Desconocido" },
            model = Build.MODEL.orEmpty().ifBlank { "Desconocido" },
            androidVersionName = Build.VERSION.RELEASE.orEmpty().ifBlank { "?" },
            androidSdkInt = Build.VERSION.SDK_INT,
            oneUiVersion = detectOneUiVersion()
        )
    }

    private fun detectOneUiVersion(): String? {
        if (!Build.MANUFACTURER.equals("samsung", ignoreCase = true)) return null

        val raw = readSystemProperty("ro.build.version.oneui") ?: return null
        return formatOneUiVersion(raw)
    }

    /**
     * Reads a system property using reflection into the hidden
     * `android.os.SystemProperties` class. This only performs a read
     * (`get`), never a write, and is wrapped defensively: on any
     * failure (property missing, reflection blocked by newer platform
     * restrictions, etc.) it simply returns null instead of crashing.
     */
    private fun readSystemProperty(key: String): String? {
        return try {
            val clazz = Class.forName("android.os.SystemProperties")
            val method = clazz.getMethod("get", String::class.java)
            val value = method.invoke(null, key) as? String
            value?.takeIf { it.isNotBlank() }
        } catch (t: Throwable) {
            // Reflection can fail across OEM/OS versions; this is expected
            // and not a bug — we simply don't know the One UI version.
            null
        }
    }

    /**
     * Samsung encodes the One UI version as a numeric string, e.g. "60100"
     * for One UI 6.0.1 or "50000" for One UI 5.0. We convert that into a
     * human friendly "X.Y" (or "X.Y.Z" when the patch digit is non-zero).
     * If the raw value doesn't match the expected pattern we return it
     * as-is so at least something informative is shown.
     */
    private fun formatOneUiVersion(raw: String): String {
        val digits = raw.filter { it.isDigit() }
        if (digits.length < 3) return "One UI $raw"

        val padded = digits.padStart(5, '0')
        val major = padded.substring(0, 2).trimStart('0').ifEmpty { "0" }
        val minor = padded.substring(2, 3)
        val patch = padded.substring(3, 4)

        return if (patch == "0") {
            "One UI $major.$minor"
        } else {
            "One UI $major.$minor.$patch"
        }
    }
}
