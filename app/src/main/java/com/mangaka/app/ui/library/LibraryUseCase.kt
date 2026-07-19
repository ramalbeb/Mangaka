package com.mangaka.app.ui.library

import com.mangaka.data.repository.MangaRepository
import com.mangaka.domain.model.Manga

class LibraryUseCase(
    private val mangaRepository: MangaRepository
) {

    suspend fun getLibrary(): List<LibraryItem> {
        val mangaList = mangaRepository.getAll()
        return mangaList.map { it.toLibraryItem() }
    }

    private fun Manga.toLibraryItem(): LibraryItem {
        val categoryName = genres.firstOrNull()?.trim().takeUnless { it.isNullOrEmpty() }
            ?: "Default"

        return LibraryItem(
            id = id,
            title = title,
            coverUrl = coverUrl,
            category = categoryName,
            lastRead = lastRead,
            lastUpdated = lastUpdated,
            dateAdded = lastUpdated,
            isDownloaded = isDownloaded,
            unreadCount = unreadCount,
            isCompleted = status.equals("completed", ignoreCase = true),
            isFavorite = isFavorite
        )
    }
}
