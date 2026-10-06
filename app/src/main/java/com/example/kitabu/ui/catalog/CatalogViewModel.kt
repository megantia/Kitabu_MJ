package com.example.kitabu.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitabu.data.local.BookEntity
import com.example.kitabu.data.repository.LibraryRepository
import com.example.kitabu.domain.isValidRentalDuration
import com.example.kitabu.domain.sanitizeUserName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel(private val repository: LibraryRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val userMessage = MutableStateFlow<String?>(null)
    private val confirmation = MutableStateFlow<ReservationConfirmation?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CatalogUiState> = combine(
        searchQuery.flatMapLatest { query -> repository.searchBooks(query) },
        searchQuery,
        userMessage,
        confirmation
    ) { books, query, message, reserved ->
        CatalogUiState(books = books, searchQuery = query, userMessage = message, confirmation = reserved)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CatalogUiState()
    )

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun reserveBook(book: BookEntity, userName: String, rentalDurationDays: Int) {
        val cleanName = sanitizeUserName(userName)
        if (cleanName.isEmpty()) {
            userMessage.update { "Enter your name to reserve a book." }
            return
        }
        if (!isValidRentalDuration(rentalDurationDays)) {
            userMessage.update { "Choose a valid rental duration." }
            return
        }
        viewModelScope.launch {
            val deadline = repository.reserveBook(book, cleanName, rentalDurationDays)
            confirmation.update { ReservationConfirmation(book.title, deadline) }
        }
    }

    fun messageShown() {
        userMessage.update { null }
    }

    fun confirmationDismissed() {
        confirmation.update { null }
    }
}
