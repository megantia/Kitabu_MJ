package com.example.kitabu.ui.catalog

import com.example.kitabu.data.local.BookEntity

data class ReservationConfirmation(val bookTitle: String, val returnDeadline: Long)

data class CatalogUiState(
    val books: List<BookEntity> = emptyList(),
    val searchQuery: String = "",
    val userMessage: String? = null,
    val confirmation: ReservationConfirmation? = null
)
