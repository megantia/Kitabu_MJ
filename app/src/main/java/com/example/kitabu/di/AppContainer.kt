package com.example.kitabu.di

import android.content.Context
import com.example.kitabu.data.local.KitabuDatabase
import com.example.kitabu.data.repository.LibraryRepository
import kotlinx.coroutines.CoroutineScope

class AppContainer(context: Context, applicationScope: CoroutineScope) {

    private val database: KitabuDatabase by lazy {
        KitabuDatabase.getDatabase(context, applicationScope)
    }

    val libraryRepository: LibraryRepository by lazy {
        LibraryRepository(database.bookDao(), database.bookingDao())
    }
}
