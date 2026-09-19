package com.example.kitabu.ui.catalog.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kitabu.data.local.BookEntity

private val AvailableGreen = Color(0xFF2E7D32)
private val BorrowedOrange = Color(0xFFEF6C00)

@Composable
fun BookCard(book: BookEntity, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = book.isAvailable, onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = book.title, style = MaterialTheme.typography.titleMedium, maxLines = 2)
            Text(text = book.author, style = MaterialTheme.typography.bodyMedium)
            Text(text = book.category, style = MaterialTheme.typography.bodySmall)
            AvailabilityBadge(isAvailable = book.isAvailable)
        }
    }
}

@Composable
private fun AvailabilityBadge(isAvailable: Boolean, modifier: Modifier = Modifier) {
    val (color, label) = if (isAvailable) AvailableGreen to "Available" else BorrowedOrange to "Borrowed"
    Text(
        text = label,
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .background(color = color, shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}
