package com.mangaka.data.repository

import com.mangaka.domain.model.Chapter
import com.mangaka.domain.model.Manga
import com.mangaka.domain.model.Page
import com.mangaka.sources.SourceManager
import com.mangaka.sources.mapper.toDomain



class SourceRepository {

    private val sources = SourceManager.list()

    fun getSources() = sources

    fun getSourceById(id: Long) = SourceManager.get(id)

    suspend fun getPopularManga(sourceId: Long): List<Manga> {
        val source = getSourceById(sourceId) ?: return emptyList()
        return source.getPopularManga().map { it.toDomain() }
    }

    suspend fun searchManga(sourceId: Long, query: String): List<Manga> {
        val source = getSourceById(sourceId) ?: return emptyList()
        return source.searchManga(query).map { it.toDomain() }
    }

    suspend fun getMangaDetails(sourceId: Long, url: String): Manga {
        val source = getSourceById(sourceId) ?: error("Source not found")
        return source.getMangaDetails(url).toDomain()
    }

    suspend fun getChapterList(sourceId: Long, mangaUrl: String): List<Chapter> {
        val source = getSourceById(sourceId) ?: return emptyList()
        return source.getChapterList(mangaUrl).map { it.toDomain(0L) }
    }

    suspend fun getPageList(sourceId: Long, chapterUrl: String): List<Page> {
        val source = getSourceById(sourceId) ?: return emptyList()
        return source.getPageList(chapterUrl).map { it.toDomain(0L) }
    }
}
