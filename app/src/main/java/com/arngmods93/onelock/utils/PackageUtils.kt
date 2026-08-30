package com.arngmods93.onelock.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log

object PackageUtils {

    private const val TAG = "PackageUtils"

    sealed interface LaunchResult {
        data object Launched : LaunchResult
        data object OpenedAppSettings : LaunchResult
        data class FellBackToDownloadPage(val browserResult: BrowserUtils.OpenResult) : LaunchResult
    }

    fun isModuleInstalled(context: Context, packageName: String): Boolean {
        return getPackageInfo(context, packageName) != null
    }

    fun getInstalledVersionName(context: Context, packageName: String): String? {
        return getPackageInfo(context, packageName)?.versionName
    }

    fun openModule(
        context: Context,
        packageName: String,
        fallbackUrl: String
    ): LaunchResult {
        if (!isModuleInstalled(context, packageName)) {
            return fallBackToDownload(context, fallbackUrl)
        }

        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            return try {
                context.startActivity(launchIntent)
                LaunchResult.Launched
            } catch (e: Exception) {
                Log.w(TAG, "Failed to launch $packageName despite resolved intent", e)
                openAppSettingsOrFallback(context, packageName, fallbackUrl)
            }
        }

        return openAppSettingsOrFallback(context, packageName, fallbackUrl)
    }

    private fun openAppSettingsOrFallback(
        context: Context,
        packageName: String,
        fallbackUrl: String
    ): LaunchResult {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        return if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
            LaunchResult.OpenedAppSettings
        } else {
            fallBackToDownload(context, fallbackUrl)
        }
    }

    private fun fallBackToDownload(context: Context, fallbackUrl: String): LaunchResult {
        val result = BrowserUtils.open(context, fallbackUrl, BrowserUtils.OpenMode.CUSTOM_TABS)
        return LaunchResult.FellBackToDownloadPage(result)
    }

    private fun getPackageInfo(context: Context, packageName: String): PackageInfo? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(packageName, 0)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}
