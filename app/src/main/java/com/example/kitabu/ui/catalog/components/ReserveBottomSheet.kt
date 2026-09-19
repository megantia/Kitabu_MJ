package com.example.kitabu.ui.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kitabu.data.local.BookEntity

private val durationChoices = listOf(7, 14, 30)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReserveBottomSheet(
    book: BookEntity,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onConfirm: (userName: String, durationDays: Int) -> Unit
) {
    var userName by remember { mutableStateOf("") }
    var selectedDuration by remember { mutableIntStateOf(durationChoices.first()) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Reserve \"${book.title}\"", style = MaterialTheme.typography.titleLarge)
            Text(text = "by ${book.author}", style = MaterialTheme.typography.bodyMedium)

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Your name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = "Rental duration", style = MaterialTheme.typography.labelLarge)
            Column(Modifier.selectableGroup()) {
                durationChoices.forEach { days ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedDuration == days,
                                onClick = { selectedDuration = days }
                            )
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(selected = selectedDuration == days, onClick = { selectedDuration = days })
                        Text(
                            text = "$days days",
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Button(
                onClick = { onConfirm(userName, selectedDuration) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Reserve")
            }
        }
    }
}
