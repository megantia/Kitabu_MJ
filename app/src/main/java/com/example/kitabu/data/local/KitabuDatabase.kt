package com.example.kitabu.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [BookEntity::class, BookingEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class KitabuDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: KitabuDatabase? = null

        fun getDatabase(context: Context, applicationScope: CoroutineScope): KitabuDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    KitabuDatabase::class.java,
                    "kitabu.db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            applicationScope.launch {
                                INSTANCE?.bookDao()?.insertBooks(sampleBooks)
                            }
                        }
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
