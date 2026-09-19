package com.example.kitabu.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kitabu.ui.dashboard.components.BookingCard
import com.example.kitabu.ui.dashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = dashboardViewModel()) {
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        if (bookings.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No active reservations yet — reserve a book from the Catalog tab.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(bookings, key = { it.booking.bookingId }) { item ->
                    BookingCard(
                        bookingWithBook = item,
                        onRenew = { viewModel.renew(item.booking.bookingId) },
                        onReturn = { viewModel.returnBook(item.booking.bookingId, item.book.bookId) },
                        onCancel = { viewModel.cancel(item.booking.bookingId, item.book.bookId) }
                    )
                }
            }
        }
    }
}
