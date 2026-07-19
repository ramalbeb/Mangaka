package com.mangaka.sources

import android.webkit.WebView
import com.mangaka.sources.models.SourceChapter
import com.mangaka.sources.models.SourceManga
import com.mangaka.sources.models.SourcePage

/**
 * Base class for all manga sources (Tachiyomi-style).
 * Each source must implement these functions.
 */
abstract class BaseSource(
    val id: Long,
    val name: String,
    val lang: String,
    val isWebView: Boolean = false,
) {

    /** Load popular manga list */
    abstract suspend fun getPopularManga(page: Int = 1): List<SourceManga>

    /** Search manga by query */
    abstract suspend fun searchManga(query: String, page: Int = 1): List<SourceManga>

    /** Load manga details + chapters */
    abstract suspend fun getMangaDetails(url: String): SourceManga

    /** Load chapter list */
    abstract suspend fun getChapterList(mangaUrl: String): List<SourceChapter>

    /** Load page list for a chapter */
    abstract suspend fun getPageList(chapterUrl: String): List<SourcePage>
}
