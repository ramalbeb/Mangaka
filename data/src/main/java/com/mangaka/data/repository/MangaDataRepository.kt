package com.mangaka.data.repository

import com.mangaka.domain.model.Chapter
import com.mangaka.domain.model.Manga
import com.mangaka.domain.model.Page

class MangaDataRepository(
    private val sourceRepo: SourceRepository,
    private val mangaRepo: MangaRepository,
    private val chapterRepo: ChapterRepository,
    private val pageRepo: PageRepository
) {

    suspend fun loadMangaOnline(sourceId: Long, url: String): Manga {
        val manga = sourceRepo.getMangaDetails(sourceId, url)
        mangaRepo.insert(manga)
        return manga
    }

    suspend fun loadChaptersOnline(sourceId: Long, mangaUrl: String, mangaId: Long): List<Chapter> {
        val chapters = sourceRepo.getChapterList(sourceId, mangaUrl)
        val updated = chapters.map { it.copy(mangaId = mangaId) }
        chapterRepo.insertAll(updated)
        return updated
    }

    suspend fun loadPagesOnline(sourceId: Long, chapterUrl: String, chapterId: Long): List<Page> {
        val pages = sourceRepo.getPageList(sourceId, chapterUrl)
        val updated = pages.map { it.copy(chapterId = chapterId) }
        pageRepo.insertAll(updated)
        return updated
    }
}
