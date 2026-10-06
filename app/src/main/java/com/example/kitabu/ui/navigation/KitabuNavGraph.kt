package com.example.kitabu.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kitabu.ui.catalog.CatalogScreen
import com.example.kitabu.ui.components.RoseStrip
import com.example.kitabu.ui.dashboard.DashboardScreen
import com.example.kitabu.ui.theme.Gold
import com.example.kitabu.ui.theme.Indigo

@Composable
fun KitabuNavGraph() {
    val navController = rememberNavController()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = { KitabuNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Catalog.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Catalog.route) {
                CatalogScreen(onViewReservations = { navController.navigateToTopLevel(Routes.Dashboard) })
            }
            composable(Routes.Dashboard.route) { DashboardScreen() }
        }
    }
}

@Composable
private fun KitabuNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column {
        RoseStrip()
        NavigationBar(containerColor = Indigo, contentColor = Color.White) {
            Routes.bottomBarItems.forEach { item ->
                val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                NavigationBarItem(
                    selected = selected,
                    onClick = { navController.navigateToTopLevel(item) },
                    icon = {
                        Icon(
                            imageVector = if (item is Routes.Catalog) Icons.Filled.Home else Icons.AutoMirrored.Filled.List,
                            contentDescription = item.label
                        )
                    },
                    label = { Text(item.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Gold,
                        selectedTextColor = Gold,
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

private fun NavHostController.navigateToTopLevel(route: Routes) {
    navigate(route.route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
