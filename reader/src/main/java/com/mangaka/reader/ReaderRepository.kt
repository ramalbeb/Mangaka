package com.mangaka.reader

import com.mangaka.data.repository.PageRepository
import com.mangaka.data.repository.SourceRepository
import com.mangaka.domain.model.Page

class ReaderRepository(
    private val sourceRepo: SourceRepository,
    private val pageRepo: PageRepository
) {

    suspend fun loadPagesOnline(sourceId: Long, chapterUrl: String, chapterId: Long): List<Page> {
        val pages = sourceRepo.getPageList(sourceId, chapterUrl)
        val updated = pages.map { it.copy(chapterId = chapterId) }
        pageRepo.insertAll(updated)
        return updated
    }

    suspend fun loadPagesOffline(chapterId: Long): List<Page> {
        return pageRepo.getPages(chapterId)
    }
}
