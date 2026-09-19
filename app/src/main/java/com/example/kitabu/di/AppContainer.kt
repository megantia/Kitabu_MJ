package com.example.kitabu.di

import android.content.Context
import com.example.kitabu.data.local.KitabuDatabase
import com.example.kitabu.data.repository.LibraryRepository
import kotlinx.coroutines.CoroutineScope

/**
 * Minimal manual service locator (chosen over Hilt to keep the KSP/annotation-processor
 * surface small under time pressure — see README for the trade-off notes).
 */
class AppContainer(context: Context, applicationScope: CoroutineScope) {

    private val database: KitabuDatabase by lazy {
        KitabuDatabase.getDatabase(context, applicationScope)
    }

    val libraryRepository: LibraryRepository by lazy {
        LibraryRepository(database.bookDao(), database.bookingDao())
    }
}
