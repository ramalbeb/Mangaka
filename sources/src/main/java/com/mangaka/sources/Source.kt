package com.mangaka.sources

import com.mangaka.sources.models.SourceChapter
import com.mangaka.sources.models.SourceManga
import com.mangaka.sources.models.SourcePage

interface Source {
    val id: Long
    val name: String
    val lang: String
    val baseUrl: String

    suspend fun getPopularManga(): List<SourceManga>
    suspend fun searchManga(query: String): List<SourceManga>
    suspend fun getMangaDetails(mangaUrl: String): SourceManga
    suspend fun getChapterList(mangaUrl: String): List<SourceChapter>
    suspend fun getPageList(chapterUrl: String): List<SourcePage>
}
