package com.example.kitabu.ui.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kitabu.data.local.BookingStatus
import com.example.kitabu.data.local.BookingWithBook
import com.example.kitabu.domain.daysRemaining
import com.example.kitabu.domain.formatDate
import com.example.kitabu.ui.theme.AvailableGreen
import com.example.kitabu.ui.theme.DeepIndigo
import com.example.kitabu.ui.theme.Gold
import com.example.kitabu.ui.theme.Indigo
import com.example.kitabu.ui.theme.OnAvailableGreen
import com.example.kitabu.ui.theme.UnavailableRed

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
    val overdue = remaining < 0

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = DeepIndigo),
        border = BorderStroke(0.5.dp, Indigo.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = if (booking.status == BookingStatus.PENDING) "Pending" else "Active",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = Indigo,
                modifier = Modifier
                    .background(Gold, RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            )
            Text(text = book.title, style = MaterialTheme.typography.titleMedium, color = DeepIndigo)
            Text(text = "by ${book.author}", style = MaterialTheme.typography.bodyMedium, color = Indigo)
            Text(
                text = "Reserved ${formatDate(booking.bookingDate)} · Return by ${formatDate(booking.returnDeadline)}",
                style = MaterialTheme.typography.bodySmall,
                color = Indigo
            )
            Text(
                text = if (overdue) "${-remaining} day(s) overdue" else "$remaining day(s) remaining",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (overdue) UnavailableRed else Indigo,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onRenew,
                    colors = ButtonDefaults.buttonColors(containerColor = AvailableGreen, contentColor = OnAvailableGreen)
                ) { Text("Renew", fontWeight = FontWeight.Medium) }
                Button(
                    onClick = onReturn,
                    colors = ButtonDefaults.buttonColors(containerColor = AvailableGreen, contentColor = OnAvailableGreen)
                ) { Text("Return", fontWeight = FontWeight.Medium) }
                if (booking.status == BookingStatus.PENDING) {
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Indigo)
                    ) { Text("Cancel", fontWeight = FontWeight.Medium) }
                }
            }
        }
    }
}
