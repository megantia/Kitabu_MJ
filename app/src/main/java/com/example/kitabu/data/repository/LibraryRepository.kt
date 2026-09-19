package com.example.kitabu.data.repository

import com.example.kitabu.data.local.BookDao
import com.example.kitabu.data.local.BookEntity
import com.example.kitabu.data.local.BookingDao
import com.example.kitabu.data.local.BookingEntity
import com.example.kitabu.data.local.BookingStatus
import com.example.kitabu.data.local.BookingWithBook
import kotlinx.coroutines.flow.Flow

/**
 * Sits between the ViewModels and the DAOs: no UI-facing code depends on Room directly,
 * and this is where the reserve/renew/return/cancel business logic lives.
 */
class LibraryRepository(
    private val bookDao: BookDao,
    private val bookingDao: BookingDao
) {
    fun observeAllBooks(): Flow<List<BookEntity>> = bookDao.getAllBooks()

    fun searchBooks(query: String): Flow<List<BookEntity>> =
        if (query.isBlank()) bookDao.getAllBooks() else bookDao.searchBooks(query.trim())

    fun observeActiveBookings(): Flow<List<BookingWithBook>> = bookingDao.getActiveBookingsWithBooks()

    suspend fun reserveBook(book: BookEntity, userName: String, rentalDurationDays: Int) {
        val now = System.currentTimeMillis()
        val deadline = now + rentalDurationDays * DAY_MILLIS
        bookingDao.insertBooking(
            BookingEntity(
                bookOwnerId = book.bookId,
                userName = userName.trim(),
                bookingDate = now,
                returnDeadline = deadline,
                status = BookingStatus.PENDING
            )
        )
        bookDao.setAvailability(book.bookId, isAvailable = false)
    }

    suspend fun renewBooking(bookingId: Long, additionalDays: Int) {
        val newDeadline = System.currentTimeMillis() + additionalDays * DAY_MILLIS
        bookingDao.renewBooking(bookingId, newDeadline)
    }

    suspend fun returnBooking(bookingId: Long, bookId: Long) {
        bookingDao.markReturned(bookingId)
        bookDao.setAvailability(bookId, isAvailable = true)
    }

    suspend fun cancelBooking(bookingId: Long, bookId: Long) {
        bookingDao.cancelBooking(bookingId)
        bookDao.setAvailability(bookId, isAvailable = true)
    }

    companion object {
        private const val DAY_MILLIS = 24 * 60 * 60 * 1000L
    }
}
