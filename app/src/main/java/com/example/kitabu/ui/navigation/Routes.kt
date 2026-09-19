package com.example.kitabu.ui.navigation

sealed class Routes(val route: String, val label: String) {
    data object Catalog : Routes("catalog", "Catalog")
    data object Dashboard : Routes("dashboard", "My Reservations")

    companion object {
        val bottomBarItems = listOf(Catalog, Dashboard)
    }
}
