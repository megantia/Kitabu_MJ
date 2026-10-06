package com.example.kitabu.ui.catalog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.kitabu.data.local.BookEntity
import com.example.kitabu.domain.MAX_NAME_LENGTH
import com.example.kitabu.domain.formatDate
import com.example.kitabu.domain.returnDateAfter
import com.example.kitabu.domain.sanitizeUserName
import com.example.kitabu.ui.catalog.ReservationConfirmation
import com.example.kitabu.ui.components.DialogCard
import com.example.kitabu.ui.theme.AvailableGreen
import com.example.kitabu.ui.theme.DeepIndigo
import com.example.kitabu.ui.theme.ErrorRed
import com.example.kitabu.ui.theme.Gold
import com.example.kitabu.ui.theme.Indigo
import com.example.kitabu.ui.theme.OnAvailableGreen
import com.example.kitabu.ui.theme.PaleGold
import com.example.kitabu.ui.theme.UnavailableRed

private val durationChoices = listOf(7, 14, 30)

@Composable
fun ReserveDialog(
    book: BookEntity,
    onDismiss: () -> Unit,
    onConfirm: (userName: String, durationDays: Int) -> Unit
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var selectedDuration by rememberSaveable { mutableIntStateOf(durationChoices.first()) }
    var showNameError by rememberSaveable { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        DialogCard(title = "Reserve this book") {
            Text(text = book.title, style = MaterialTheme.typography.titleMedium, color = DeepIndigo)
            Text(text = "by ${book.author}", style = MaterialTheme.typography.bodyMedium, color = Indigo)

            OutlinedTextField(
                value = userName,
                onValueChange = {
                    userName = it.take(MAX_NAME_LENGTH)
                    showNameError = false
                },
                label = { Text("Your name") },
                singleLine = true,
                isError = showNameError,
                supportingText = if (showNameError) {
                    { Text("Enter your name to reserve a book", color = ErrorRed, fontWeight = FontWeight.Medium) }
                } else null,
                colors = indigoFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Rental duration",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = Indigo
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                durationChoices.forEach { days ->
                    DurationChip(
                        days = days,
                        selected = days == selectedDuration,
                        onSelect = { selectedDuration = days },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PaleGold, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.DateRange, contentDescription = null, tint = Indigo, modifier = Modifier.size(18.dp))
                Text(
                    text = "Return by ${formatDate(returnDateAfter(selectedDuration))}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Indigo
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CancelButton(label = "Cancel", onClick = onDismiss, modifier = Modifier.weight(1f))
                GreenButton(
                    label = "Reserve",
                    onClick = {
                        if (sanitizeUserName(userName).isEmpty()) showNameError = true
                        else onConfirm(userName, selectedDuration)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ReservationConfirmationDialog(
    confirmation: ReservationConfirmation,
    onClose: () -> Unit,
    onViewReservations: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        DialogCard(title = "Book reserved") {
            Surface(
                shape = CircleShape,
                color = AvailableGreen,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = OnAvailableGreen,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Text(
                text = "${confirmation.bookTitle} is yours",
                style = MaterialTheme.typography.titleMedium,
                color = DeepIndigo,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Return it by ${formatDate(confirmation.returnDeadline)}.",
                style = MaterialTheme.typography.bodyMedium,
                color = Indigo,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CancelButton(label = "Close", onClick = onClose, modifier = Modifier.weight(1f))
                GreenButton(label = "View reservations", onClick = onViewReservations, modifier = Modifier.weight(1.4f))
            }
        }
    }
}

@Composable
private fun DurationChip(days: Int, selected: Boolean, onSelect: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (selected) Indigo else Color.White,
        border = BorderStroke(1.dp, Indigo),
        modifier = modifier.selectable(selected = selected, role = Role.RadioButton, onClick = onSelect)
    ) {
        Text(
            text = "$days days",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = if (selected) Gold else Indigo,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun CancelButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(1.dp, Indigo),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Indigo),
        modifier = modifier
    ) { Text(label, fontWeight = FontWeight.Medium) }
}

@Composable
private fun GreenButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = AvailableGreen, contentColor = OnAvailableGreen),
        modifier = modifier
    ) { Text(label, fontWeight = FontWeight.Medium) }
}

@Composable
private fun indigoFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = DeepIndigo,
    unfocusedTextColor = DeepIndigo,
    errorTextColor = DeepIndigo,
    focusedBorderColor = Indigo,
    unfocusedBorderColor = Indigo,
    errorBorderColor = UnavailableRed,
    focusedLabelColor = Indigo,
    unfocusedLabelColor = Indigo,
    errorLabelColor = ErrorRed,
    cursorColor = Indigo,
    errorCursorColor = ErrorRed
)
