package com.mangaka.sources

import com.mangaka.sources.models.SourceChapter
import com.mangaka.sources.models.SourceManga
import com.mangaka.sources.models.SourcePage

class FakeSource : HttpSource() {

    override val id = 1L
    override val name = "FakeSource"
    override val lang = "EN"
    override val baseUrl = "https://example.com"

    override suspend fun getPopularManga(): List<SourceManga> {
        return List(20) {
            SourceManga(
                url = "/manga/$it",
                title = "Fake Manga $it",
                coverUrl = "https://picsum.photos/200/300?random=$it"
            )
        }
    }

    override suspend fun searchManga(query: String): List<SourceManga> {
        return getPopularManga().filter { it.title.contains(query, true) }
    }

    override suspend fun getMangaDetails(mangaUrl: String): SourceManga {
        return SourceManga(
            url = mangaUrl,
            title = "Fake Manga",
            coverUrl = "https://picsum.photos/200/300"
        )
    }

    override suspend fun getChapterList(mangaUrl: String): List<SourceChapter> {
        return List(20) {
            SourceChapter(
                url = "/chapter/$it",
                title = "Chapter ${it + 1}",
                number = it + 1f
            )
        }
    }

    override suspend fun getPageList(chapterUrl: String): List<SourcePage> {
        return List(20) {
            SourcePage(
                index = it,
                imageUrl = "https://picsum.photos/800/1200?random=$it"
            )
        }
    }
}
