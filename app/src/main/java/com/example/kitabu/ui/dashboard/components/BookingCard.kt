package com.example.kitabu.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kitabu.data.local.BookingStatus
import com.example.kitabu.data.local.BookingWithBook
import com.example.kitabu.domain.daysRemaining
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

@Composable
fun BookingCard(
    bookingWithBook: BookingWithBook,
    onRenew: () -> Unit,
    onReturn: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val booking = bookingWithBook.booking
    val book = bookingWithBook.book
    val remaining = daysRemaining(booking.returnDeadline)

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = book.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "by ${book.author}", style = MaterialTheme.typography.bodySmall)
            Text(
                text = "Reserved: ${dateFormat.format(Date(booking.bookingDate))}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Return by: ${dateFormat.format(Date(booking.returnDeadline))}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = if (remaining >= 0) "$remaining day(s) remaining" else "${-remaining} day(s) overdue",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = onRenew) { Text("Renew") }
                Button(onClick = onReturn) { Text("Return") }
                if (booking.status == BookingStatus.PENDING) {
                    TextButton(onClick = onCancel) { Text("Cancel") }
                }
            }
        }
    }
}
