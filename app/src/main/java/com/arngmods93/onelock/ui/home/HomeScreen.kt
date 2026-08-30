package com.arngmods93.onelock.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arngmods93.onelock.R
import com.arngmods93.onelock.data.model.CompatibilityStatus
import com.arngmods93.onelock.data.model.GoodLockModule
import com.arngmods93.onelock.data.model.ModuleCategory
import com.arngmods93.onelock.ui.components.CategoryChip
import com.arngmods93.onelock.ui.components.DeviceInfoCard
import com.arngmods93.onelock.ui.components.ModuleCard
import com.arngmods93.onelock.ui.components.OneLockSearchBar
import com.arngmods93.onelock.utils.CompatibilityChecker
import com.arngmods93.onelock.utils.DeviceInfo

@Composable
fun HomeScreen(
    onModuleClick: (GoodLockModule) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.setDevice(DeviceInfo.current())
    }

    // Refresh installation status when screen becomes visible or when query/category changes
    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(uiState.query, uiState.selectedCategory) {
        viewModel.refresh()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.app_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            uiState.device?.let { device ->
                item { DeviceInfoCard(device = device) }
            }

            item {
                OneLockSearchBar(
                    query = uiState.query,
                    onQueryChange = viewModel::onQueryChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        CategoryChip(
                            label = stringResource(R.string.all_categories),
                            selected = uiState.selectedCategory == null,
                            onClick = { viewModel.onCategorySelected(null) }
                        )
                    }
                    items(ModuleCategory.entries.toList()) { category ->
                        CategoryChip(
                            label = category.displayName,
                            selected = uiState.selectedCategory == category,
                            onClick = { viewModel.onCategorySelected(category) }
                        )
                    }
                }
            }

            if (uiState.modules.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.no_results),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(uiState.modules, key = { it.id }) { module ->
                    val device = uiState.device
                    val compatibility = if (device != null) {
                        CompatibilityChecker.check(device, module)
                    } else {
                        CompatibilityStatus.UNKNOWN
                    }
                    ModuleCard(
                        module = module,
                        compatibility = compatibility,
                        onClick = { onModuleClick(module) }
                    )
                }
            }
        }
    }
}
