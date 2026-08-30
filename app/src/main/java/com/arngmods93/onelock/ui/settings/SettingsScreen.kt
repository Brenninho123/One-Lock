package com.arngmods93.onelock.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arngmods93.onelock.BuildConfig
import com.arngmods93.onelock.R
import com.arngmods93.onelock.utils.BrowserUtils

private const val REPOSITORY_URL = "https://github.com/ArngMods93/One-Lock"
private const val ISSUES_URL = "https://github.com/ArngMods93/One-Lock/issues"
private const val LICENSE_URL = "https://github.com/ArngMods93/One-Lock/blob/main/LICENSE"

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.nav_settings),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        SettingsSection(
            icon = Icons.Filled.Info,
            title = stringResource(R.string.settings_about_title),
            body = stringResource(R.string.settings_about_body)
        )

        VersionCard()

        SettingsSection(
            icon = Icons.Filled.VerifiedUser,
            title = stringResource(R.string.settings_compat_title),
            body = stringResource(R.string.settings_compat_body)
        )

        SettingsSection(
            icon = Icons.Filled.Gavel,
            title = stringResource(R.string.settings_disclaimer_title),
            body = stringResource(R.string.settings_disclaimer_body)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        ExternalLinkButton(
            icon = Icons.Filled.Code,
            label = stringResource(R.string.settings_repository),
            url = REPOSITORY_URL
        )

        ExternalLinkButton(
            icon = Icons.Filled.BugReport,
            label = stringResource(R.string.settings_report_issue),
            url = ISSUES_URL
        )

        ExternalLinkButton(
            icon = Icons.Filled.Gavel,
            label = stringResource(R.string.settings_license),
            url = LICENSE_URL
        )
    }
}

@Composable
private fun VersionCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${stringResource(R.string.settings_version)}: ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${stringResource(R.string.settings_build)}: ${BuildConfig.VERSION_CODE} (${BuildConfig.BUILD_TYPE})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun ExternalLinkButton(
    icon: ImageVector,
    label: String,
    url: String
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    val invalidUrlMessage = R.string.error_invalid_url
    val blockedSchemeMessage = R.string.error_blocked_scheme
    val noBrowserMessage = R.string.error_no_browser
    val genericErrorMessage = R.string.error_generic

    OutlinedButton(
        onClick = {
            isLoading = true
            BrowserUtils.openWithFeedback(
                context = context,
                url = url,
                invalidUrlMessage = invalidUrlMessage,
                blockedSchemeMessage = blockedSchemeMessage,
                noBrowserMessage = noBrowserMessage,
                genericErrorMessage = genericErrorMessage
            )
            isLoading = false
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null)
            Text(text = label, modifier = Modifier.padding(start = 0.dp))
            AnimatedVisibility(visible = !isLoading) {
                Icon(
                    Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    icon: ImageVector,
    title: String,
    body: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}
