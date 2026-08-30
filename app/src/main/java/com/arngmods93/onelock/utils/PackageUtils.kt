package com.arngmods93.onelock.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings

object PackageUtils {

    fun isModuleInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun openModule(context: Context, packageName: String, fallbackUrl: String) {
        if (!isModuleInstalled(context, packageName)) {
            BrowserUtils.openDownloadPage(context, fallbackUrl)
            return
        }

        val packageManager = context.packageManager

        // 1. Try to get Launch Intent
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
            return
        }

        // 2. Try to open App Info in Settings
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Last fallback to APKMirror
            BrowserUtils.openDownloadPage(context, fallbackUrl)
        }
    }
}
