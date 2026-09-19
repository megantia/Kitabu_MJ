package com.example.kitabu.data.local

import androidx.room.Embedded

data class BookingWithBook(
    @Embedded val booking: BookingEntity,
    @Embedded(prefix = "book_") val book: BookEntity
)
