package com.arngmods93.onelock.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arngmods93.onelock.R
import com.arngmods93.onelock.data.ModuleCatalog
import com.arngmods93.onelock.ui.details.ModuleDetailScreen
import com.arngmods93.onelock.ui.home.HomeScreen
import com.arngmods93.onelock.ui.settings.SettingsScreen
import com.arngmods93.onelock.utils.PackageUtils
import androidx.compose.ui.platform.LocalContext

private data class TopLevelTab(val route: String, val labelRes: Int, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val topLevelTabs = listOf(
    TopLevelTab(OneLockDestinations.HOME, R.string.nav_home, Icons.Filled.Home),
    TopLevelTab(OneLockDestinations.SETTINGS, R.string.nav_settings, Icons.Filled.Settings)
)

@Composable
fun OneLockNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val showBottomBar = topLevelTabs.any { tab ->
        currentDestination?.hierarchy?.any { it.route == tab.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    topLevelTabs.forEach { tab ->
                        val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(stringResource(tab.labelRes)) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        val context = LocalContext.current
        NavHost(
            navController = navController,
            startDestination = OneLockDestinations.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(OneLockDestinations.HOME) {
                HomeScreen(
                    onModuleClick = { module ->
                        PackageUtils.openModule(context, module.packageName, module.apkMirrorUrl)
                    }
                )
            }

            composable(OneLockDestinations.SETTINGS) {
                SettingsScreen()
            }

            composable(OneLockDestinations.MODULE_DETAIL) { backStackEntry ->
                val moduleId = backStackEntry.arguments?.getString(OneLockDestinations.MODULE_ID_ARG).orEmpty()
                ModuleDetailScreen(
                    moduleId = moduleId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
