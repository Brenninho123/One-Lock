package com.arngmods93.onelock.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arngmods93.onelock.BuildConfig
import com.arngmods93.onelock.R
import com.arngmods93.onelock.utils.BrowserUtils

private const val REPOSITORY_URL = "https://github.com/ArngMods93/One-Lock"

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val invalidUrlMessage = stringResource(R.string.error_invalid_url)
    val noBrowserMessage = stringResource(R.string.error_no_browser)

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
            }
        }

        OutlinedButton(
            onClick = {
                BrowserUtils.openDownloadPageWithFeedback(
                    context = context,
                    url = REPOSITORY_URL,
                    invalidUrlMessage = invalidUrlMessage,
                    noBrowserMessage = noBrowserMessage
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Code, contentDescription = null)
            Text(
                text = "  ${stringResource(R.string.settings_repository)}",
                modifier = Modifier.padding(start = 4.dp)
            )
        }

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
    }
}

@Composable
private fun SettingsSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
