package com.arngmods93.onelock.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabsIntent
import android.widget.Toast

object BrowserUtils {

    private const val TAG = "BrowserUtils"

    private val ALLOWED_SCHEMES = setOf("http", "https")

    sealed interface OpenResult {
        data object Success : OpenResult
        data object InvalidUrl : OpenResult
        data object BlockedScheme : OpenResult
        data object NoBrowserAvailable : OpenResult
        data class Failed(val throwable: Throwable) : OpenResult
    }

    enum class OpenMode {
        CUSTOM_TABS,
        EXTERNAL_BROWSER
    }

    fun validate(url: String): OpenResult? {
        if (url.isBlank()) return OpenResult.InvalidUrl
        val uri = runCatching { Uri.parse(url) }.getOrNull() ?: return OpenResult.InvalidUrl
        val scheme = uri.scheme?.lowercase() ?: return OpenResult.InvalidUrl
        if (scheme !in ALLOWED_SCHEMES) return OpenResult.BlockedScheme
        if (uri.host.isNullOrBlank()) return OpenResult.InvalidUrl
        return null
    }

    fun canHandle(context: Context, uri: Uri): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        return intent.resolveActivity(context.packageManager) != null
    }

    fun open(
        context: Context,
        url: String,
        mode: OpenMode = OpenMode.CUSTOM_TABS
    ): OpenResult {
        validate(url)?.let { return it }

        val uri = Uri.parse(url)
        if (!canHandle(context, uri)) return OpenResult.NoBrowserAvailable

        return try {
            when (mode) {
                OpenMode.CUSTOM_TABS -> openWithCustomTabs(context, uri)
                OpenMode.EXTERNAL_BROWSER -> openWithExternalIntent(context, uri)
            }
            OpenResult.Success
        } catch (e: ActivityNotFoundException) {
            Log.w(TAG, "No activity found to handle $url")
            OpenResult.NoBrowserAvailable
        } catch (e: SecurityException) {
            Log.w(TAG, "Blocked by system security policy for $url", e)
            OpenResult.Failed(e)
        }
    }

    private fun openWithCustomTabs(context: Context, uri: Uri) {
        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        intent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.launchUrl(context, uri)
    }

    private fun openWithExternalIntent(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    fun openWithFeedback(
        context: Context,
        url: String,
        mode: OpenMode = OpenMode.CUSTOM_TABS,
        @StringRes invalidUrlMessage: Int,
        @StringRes blockedSchemeMessage: Int,
        @StringRes noBrowserMessage: Int,
        @StringRes genericErrorMessage: Int
    ): OpenResult {
        val result = open(context, url, mode)
        val messageRes = when (result) {
            OpenResult.Success -> null
            OpenResult.InvalidUrl -> invalidUrlMessage
            OpenResult.BlockedScheme -> blockedSchemeMessage
            OpenResult.NoBrowserAvailable -> noBrowserMessage
            is OpenResult.Failed -> genericErrorMessage
        }
        messageRes?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        return result
    }
}
