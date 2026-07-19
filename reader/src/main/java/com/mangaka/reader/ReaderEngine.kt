package com.mangaka.reader

import com.mangaka.domain.model.Page
import com.mangaka.sources.BaseSource
import com.mangaka.sources.mapper.toDomain


class ReaderEngine(
    private val source: BaseSource
) {

    suspend fun loadPages(chapterUrl: String): List<Page> {
        val pages = source.getPageList(chapterUrl)
        return pages.map { it.toDomain(0L) }
    }
}
