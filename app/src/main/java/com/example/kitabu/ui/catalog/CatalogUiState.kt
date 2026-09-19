package com.example.kitabu.ui.catalog

import com.example.kitabu.data.local.BookEntity

data class CatalogUiState(
    val books: List<BookEntity> = emptyList(),
    val searchQuery: String = "",
    val userMessage: String? = null
)
