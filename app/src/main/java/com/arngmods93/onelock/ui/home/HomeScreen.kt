package com.arngmods93.onelock.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onModuleClick: (GoodLockModule) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.setDevice(DeviceInfo.current())
    }

    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    LaunchedEffect(uiState.query, uiState.selectedCategory) {
        viewModel.refresh()
        listState.animateScrollToItem(0)
    }

    val compatibilityCache = remember(uiState.modules, uiState.device) {
        val device = uiState.device
        uiState.modules.associate { module ->
            module.id to if (device != null) {
                CompatibilityChecker.check(device, module)
            } else {
                CompatibilityStatus.UNKNOWN
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "header") {
                HomeHeader()
            }

            uiState.device?.let { device ->
                item(key = "device_info") {
                    DeviceInfoCard(device = device)
                }
            }

            item(key = "search") {
                OneLockSearchBar(
                    query = uiState.query,
                    onQueryChange = viewModel::onQueryChange,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item(key = "categories") {
                CategoryRow(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = viewModel::onCategorySelected
                )
            }

            when {
                uiState.isLoading && uiState.modules.isEmpty() -> {
                    item(key = "loading") {
                        LoadingState()
                    }
                }

                uiState.modules.isEmpty() -> {
                    item(key = "empty") {
                        EmptyState()
                    }
                }

                else -> {
                    items(uiState.modules, key = { it.id }) { module ->
                        ModuleCard(
                            module = module,
                            compatibility = compatibilityCache[module.id] ?: CompatibilityStatus.UNKNOWN,
                            onClick = { onModuleClick(module) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Column {
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
}

@Composable
private fun CategoryRow(
    selectedCategory: ModuleCategory?,
    onCategorySelected: (ModuleCategory?) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item(key = "category_all") {
            CategoryChip(
                label = stringResource(R.string.all_categories),
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) }
            )
        }
        items(ModuleCategory.entries.toList(), key = { it.name }) { category ->
            CategoryChip(
                label = category.displayName,
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_results),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
