package com.example.kitabu.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kitabu.KitabuApplication
import com.example.kitabu.data.repository.LibraryRepository
import com.example.kitabu.ui.catalog.CatalogViewModel
import com.example.kitabu.ui.dashboard.DashboardViewModel

@Composable
private fun libraryRepository(): LibraryRepository {
    val application = LocalContext.current.applicationContext as KitabuApplication
    return application.container.libraryRepository
}

@Composable
fun catalogViewModel(): CatalogViewModel {
    val repository = libraryRepository()
    return viewModel(factory = viewModelFactory { initializer { CatalogViewModel(repository) } })
}

@Composable
fun dashboardViewModel(): DashboardViewModel {
    val repository = libraryRepository()
    return viewModel(factory = viewModelFactory { initializer { DashboardViewModel(repository) } })
}
