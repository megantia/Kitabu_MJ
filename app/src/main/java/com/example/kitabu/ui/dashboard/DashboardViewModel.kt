package com.example.kitabu.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kitabu.data.local.BookingWithBook
import com.example.kitabu.data.repository.LibraryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: LibraryRepository) : ViewModel() {

    val bookings: StateFlow<List<BookingWithBook>> = repository.observeActiveBookings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun renew(bookingId: Long, additionalDays: Int = 7) {
        viewModelScope.launch { repository.renewBooking(bookingId, additionalDays) }
    }

    fun returnBook(bookingId: Long, bookId: Long) {
        viewModelScope.launch { repository.returnBooking(bookingId, bookId) }
    }

    fun cancel(bookingId: Long, bookId: Long) {
        viewModelScope.launch { repository.cancelBooking(bookingId, bookId) }
    }
}
