package com.mangaka.reader

import com.mangaka.domain.model.Page

class ReaderUseCase(
    private val repo: ReaderRepository
) {

    suspend fun loadChapterPages(
        sourceId: Long,
        chapterUrl: String,
        chapterId: Long
    ): List<Page> {

        // Try offline first
        val cached = repo.loadPagesOffline(chapterId)
        if (cached.isNotEmpty()) return cached

        // Otherwise load online
        return repo.loadPagesOnline(sourceId, chapterUrl, chapterId)
    }
}
