package com.arngmods93.onelock.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Small helper responsible for the *only* external action One Lock ever
 * performs: opening a module's download page in the user's default
 * browser. One Lock never downloads, installs, or silently fetches
 * anything itself.
 */
object BrowserUtils {

    sealed interface OpenResult {
        data object Success : OpenResult
        data object InvalidUrl : OpenResult
        data object NoBrowserAvailable : OpenResult
    }

    fun openDownloadPage(context: Context, url: String): OpenResult {
        if (url.isBlank()) return OpenResult.InvalidUrl

        val uri = runCatching { Uri.parse(url) }.getOrNull() ?: return OpenResult.InvalidUrl
        if (uri.scheme != "http" && uri.scheme != "https") return OpenResult.InvalidUrl

        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        return try {
            context.startActivity(intent)
            OpenResult.Success
        } catch (e: ActivityNotFoundException) {
            OpenResult.NoBrowserAvailable
        }
    }

    /** Convenience wrapper that also shows a Toast for error cases. */
    fun openDownloadPageWithFeedback(
        context: Context,
        url: String,
        invalidUrlMessage: String,
        noBrowserMessage: String
    ) {
        when (openDownloadPage(context, url)) {
            OpenResult.Success -> Unit
            OpenResult.InvalidUrl -> Toast.makeText(context, invalidUrlMessage, Toast.LENGTH_SHORT).show()
            OpenResult.NoBrowserAvailable -> Toast.makeText(context, noBrowserMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
