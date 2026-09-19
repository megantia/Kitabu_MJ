package com.example.kitabu.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("SELECT * FROM books ORDER BY title ASC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE isAvailable = 1 ORDER BY title ASC")
    fun getAvailableBooks(): Flow<List<BookEntity>>

    @Query(
        """
        SELECT * FROM books
        WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'
        ORDER BY title ASC
        """
    )
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Query("UPDATE books SET isAvailable = :isAvailable WHERE bookId = :bookId")
    suspend fun setAvailability(bookId: Long, isAvailable: Boolean)

    @Query("SELECT COUNT(*) FROM books")
    suspend fun countBooks(): Int
}
