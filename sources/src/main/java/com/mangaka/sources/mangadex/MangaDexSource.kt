package com.mangaka.sources.mangadex

import com.mangaka.sources.BaseSource
import com.mangaka.sources.models.SourceChapter
import com.mangaka.sources.models.SourceManga
import com.mangaka.sources.models.SourcePage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MangaDexSource : BaseSource(
    id = 1001L,
    name = "MangaDex",
    lang = "en",
    isWebView = false
) {

    override suspend fun getPopularManga(page: Int): List<SourceManga> =
        withContext(Dispatchers.IO) {
            val json = MangaDexApi.getPopularMangaEnglish()
            parse(json)
        }

    override suspend fun searchManga(query: String, page: Int): List<SourceManga> =
        withContext(Dispatchers.IO) {
            val json = MangaDexApi.request(
                "https://api.mangadex.org/manga?limit=20&title=$query"
            )
            parse(json)
        }

    // For now we can return minimal / empty data so app doesn’t crash.
    override suspend fun getMangaDetails(url: String): SourceManga =
        withContext(Dispatchers.IO) {
            // MangaDex details API can be wired later.
            SourceManga(
                url = url,
                title = "Unknown",
                coverUrl = "",
                description = "",
                status = ""
            )
        }

    override suspend fun getChapterList(mangaUrl: String): List<SourceChapter> =
        withContext(Dispatchers.IO) {
            emptyList()
        }

    override suspend fun getPageList(chapterUrl: String): List<SourcePage> =
        withContext(Dispatchers.IO) {
            emptyList()
        }

    private fun parse(json: String): List<SourceManga> {
        val root = JSONObject(json)
        val data = root.optJSONArray("data") ?: return emptyList()

        val list = mutableListOf<SourceManga>()

        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            val id = obj.getString("id")
            val attrs = obj.getJSONObject("attributes")
            val title = attrs.getJSONObject("title").optString("en", "Unknown")

            list += SourceManga(
                url = "https://mangadex.org/title/$id",
                title = title,
                coverUrl = ""
            )
        }

        return list
    }
}
