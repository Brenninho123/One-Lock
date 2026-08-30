package com.arngmods93.onelock.data.model

/**
 * Informative-only compatibility signal shown next to each module.
 * This is never used to block or alter app behaviour — it only helps
 * the user decide whether to check the download page.
 */
enum class CompatibilityStatus {
    COMPATIBLE,
    UNKNOWN,
    POSSIBLY_INCOMPATIBLE
}
