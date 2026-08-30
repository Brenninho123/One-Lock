package com.arngmods93.onelock.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val OneLockDarkColorScheme = darkColorScheme(
    primary = OneLockBlue,
    onPrimary = OneLockNavyBackground,
    secondary = OneLockBlueDark,
    background = OneLockNavyBackground,
    onBackground = OnDarkPrimaryText,
    surface = OneLockSurfaceDark,
    onSurface = OnDarkPrimaryText,
    surfaceVariant = OneLockSurfaceDark,
    onSurfaceVariant = OnDarkSecondaryText
)

private val OneLockLightColorScheme = lightColorScheme(
    primary = OneLockBlue,
    onPrimary = OneLockSurfaceLight,
    secondary = OneLockBlueDark,
    background = OneLockBackgroundLight,
    onBackground = OnLightPrimaryText,
    surface = OneLockSurfaceLight,
    onSurface = OnLightPrimaryText,
    surfaceVariant = OneLockBackgroundLight,
    onSurfaceVariant = OnLightSecondaryText
)

/**
 * One Lock's own Material 3 theme. Dynamic color (Android 12+) is
 * intentionally opt-in via [useDynamicColor] and defaults to off so the
 * app keeps a consistent identity instead of blending into One UI itself.
 */
@Composable
fun OneLockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> OneLockDarkColorScheme
        else -> OneLockLightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val activity = view.context as? Activity
        androidx.compose.runtime.SideEffect {
            activity?.window?.let { window ->
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OneLockTypography,
        content = content
    )
}
