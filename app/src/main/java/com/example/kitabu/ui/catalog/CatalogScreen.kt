package com.example.kitabu.ui.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kitabu.data.local.BookEntity
import com.example.kitabu.ui.catalog.components.BookCard
import com.example.kitabu.ui.catalog.components.ReservationConfirmationDialog
import com.example.kitabu.ui.catalog.components.ReserveDialog
import com.example.kitabu.ui.catalogViewModel
import com.example.kitabu.ui.components.TextileBand
import com.example.kitabu.ui.theme.DeepIndigo
import com.example.kitabu.ui.theme.Gold
import com.example.kitabu.ui.theme.Indigo
import com.example.kitabu.ui.theme.Lavender

@Composable
fun CatalogScreen(
    onViewReservations: () -> Unit,
    viewModel: CatalogViewModel = catalogViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var bookBeingReserved by remember { mutableStateOf<BookEntity?>(null) }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.messageShown()
        }
    }

    Scaffold(
        containerColor = Lavender,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            TextileBand {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    placeholder = { Text("Search by title or author") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = DeepIndigo,
                        unfocusedTextColor = DeepIndigo,
                        focusedPlaceholderColor = Indigo,
                        unfocusedPlaceholderColor = Indigo,
                        focusedLeadingIconColor = Indigo,
                        unfocusedLeadingIconColor = Indigo,
                        focusedBorderColor = Gold,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Indigo
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.books, key = { it.bookId }) { book ->
                    BookCard(
                        book = book,
                        onClick = { bookBeingReserved = book },
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }

    bookBeingReserved?.let { book ->
        ReserveDialog(
            book = book,
            onDismiss = { bookBeingReserved = null },
            onConfirm = { userName, durationDays ->
                viewModel.reserveBook(book, userName, durationDays)
                bookBeingReserved = null
            }
        )
    }

    uiState.confirmation?.let { confirmation ->
        ReservationConfirmationDialog(
            confirmation = confirmation,
            onClose = viewModel::confirmationDismissed,
            onViewReservations = {
                viewModel.confirmationDismissed()
                onViewReservations()
            }
        )
    }
}
