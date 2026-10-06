package com.example.kitabu.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kitabu.ui.components.TextileBand
import com.example.kitabu.ui.dashboard.components.BookingCard
import com.example.kitabu.ui.dashboardViewModel
import com.example.kitabu.ui.theme.DeepIndigo
import com.example.kitabu.ui.theme.Gold
import com.example.kitabu.ui.theme.Lavender

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = dashboardViewModel()) {
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Lavender,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            TextileBand {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "My reservations",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = "${bookings.size} active",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gold
                    )
                }
            }

            if (bookings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No active reservations yet. Reserve a book from the Catalog tab.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DeepIndigo,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
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
}
