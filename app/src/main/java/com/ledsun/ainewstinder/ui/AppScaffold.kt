package com.ledsun.ainewstinder.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ledsun.ainewstinder.R
import com.ledsun.ainewstinder.viewmodel.FeedViewModel

sealed class Screen(val route: String) {
    object Inbox : Screen("inbox")
    object Good : Screen("good")
    object Excluded : Screen("excluded")
    object Settings : Screen("settings")
}

@Composable
fun AppScaffold(viewModel: FeedViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val tabs = listOf(
        Triple(Screen.Inbox, stringResource(R.string.tab_inbox), Icons.Filled.Inbox),
        Triple(Screen.Good, stringResource(R.string.tab_good), Icons.Filled.Star),
        Triple(Screen.Excluded, stringResource(R.string.tab_excluded), Icons.Filled.Block),
        Triple(Screen.Settings, stringResource(R.string.tab_settings), Icons.Filled.Settings),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { (screen, label, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Inbox.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Inbox.route) { InboxScreen(viewModel) }
            composable(Screen.Good.route) { GoodScreen(viewModel) }
            composable(Screen.Excluded.route) { ExcludedScreen(viewModel) }
            composable(Screen.Settings.route) { SettingsScreen(viewModel) }
        }
    }
}
