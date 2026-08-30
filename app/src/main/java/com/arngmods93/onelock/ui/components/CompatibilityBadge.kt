package com.arngmods93.onelock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arngmods93.onelock.R
import com.arngmods93.onelock.data.model.CompatibilityStatus
import com.arngmods93.onelock.ui.theme.CompatibleGreen
import com.arngmods93.onelock.ui.theme.IncompatibleRed
import com.arngmods93.onelock.ui.theme.UnknownAmber

@Composable
fun CompatibilityBadge(
    status: CompatibilityStatus,
    modifier: Modifier = Modifier
) {
    val (emoji, labelRes, color) = when (status) {
        CompatibilityStatus.COMPATIBLE -> Triple("🟢", R.string.compat_compatible, CompatibleGreen)
        CompatibilityStatus.UNKNOWN -> Triple("🟡", R.string.compat_unknown, UnknownAmber)
        CompatibilityStatus.POSSIBLY_INCOMPATIBLE -> Triple("🔴", R.string.compat_incompatible, IncompatibleRed)
    }

    Row(
        modifier = modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$emoji ${stringResource(labelRes)}",
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}
