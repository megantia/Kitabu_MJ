package com.example.kitabu.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {

    @Insert
    suspend fun insertBooking(booking: BookingEntity): Long

    @Query(
        """
        SELECT
            bookings.bookingId AS bookingId,
            bookings.bookOwnerId AS bookOwnerId,
            bookings.userName AS userName,
            bookings.bookingDate AS bookingDate,
            bookings.returnDeadline AS returnDeadline,
            bookings.status AS status,
            books.bookId AS book_bookId,
            books.title AS book_title,
            books.author AS book_author,
            books.category AS book_category,
            books.isAvailable AS book_isAvailable
        FROM bookings
        INNER JOIN books ON bookings.bookOwnerId = books.bookId
        WHERE bookings.status != 'RETURNED'
        ORDER BY bookings.returnDeadline ASC
        """
    )
    fun getActiveBookingsWithBooks(): Flow<List<BookingWithBook>>

    @Query("UPDATE bookings SET returnDeadline = :newDeadline, status = 'ACTIVE' WHERE bookingId = :bookingId")
    suspend fun renewBooking(bookingId: Long, newDeadline: Long)

    @Query("UPDATE bookings SET status = 'RETURNED' WHERE bookingId = :bookingId")
    suspend fun markReturned(bookingId: Long)

    @Query("DELETE FROM bookings WHERE bookingId = :bookingId")
    suspend fun cancelBooking(bookingId: Long)
}
