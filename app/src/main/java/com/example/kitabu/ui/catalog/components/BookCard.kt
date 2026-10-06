package com.example.kitabu.ui.catalog.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kitabu.data.local.BookEntity
import com.example.kitabu.ui.theme.AvailableGreen
import com.example.kitabu.ui.theme.DeepIndigo
import com.example.kitabu.ui.theme.Indigo
import com.example.kitabu.ui.theme.OnAvailableGreen
import com.example.kitabu.ui.theme.UnavailableRed

@Composable
fun BookCard(book: BookEntity, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = book.isAvailable, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = DeepIndigo),
        border = BorderStroke(0.5.dp, Indigo.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Fixed line counts keep every card the same height, so grid rows line up.
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium,
                color = DeepIndigo,
                minLines = 3,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodyMedium,
                color = Indigo,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.category,
                style = MaterialTheme.typography.bodySmall,
                color = Indigo,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            AvailabilityBadge(isAvailable = book.isAvailable)
        }
    }
}

@Composable
private fun AvailabilityBadge(isAvailable: Boolean, modifier: Modifier = Modifier) {
    val background = if (isAvailable) AvailableGreen else UnavailableRed
    val textColor = if (isAvailable) OnAvailableGreen else Color.White
    Text(
        text = if (isAvailable) "Available" else "Unavailable",
        color = textColor,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .padding(top = 4.dp)
            .background(color = background, shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 2.dp)
    )
}
