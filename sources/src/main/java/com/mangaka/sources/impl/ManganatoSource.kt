package com.mangaka.sources.impl

import android.util.Log
import com.mangaka.engine.WebViewEngine
import com.mangaka.sources.BaseSource
import com.mangaka.sources.models.SourceChapter
import com.mangaka.sources.models.SourceManga
import com.mangaka.sources.models.SourcePage
import org.jsoup.Jsoup

class ManganatoSource(
    private val engine: WebViewEngine
) : BaseSource(
    id = 4001L,
    name = "Manganato",
    lang = "en",
    isWebView = true
) {

    private val base = "https://manganato.com"

    private fun absUrl(href: String?): String {
        val h = href.orEmpty()
        return if (h.startsWith("http")) h else {
            if (h.startsWith("//")) "https:$h"
            else if (h.startsWith("/")) "https://readmanganato.com$h"
            else "https://readmanganato.com/$h"
        }
    }

    override suspend fun getPopularManga(page: Int): List<SourceManga> {
        // Top viewed list
        val url = "https://manganato.com/genre-all/$page?type=topview"
        Log.e("MANGANATO", "Fetching Popular: $url")
        val html = engine.loadHtml(url)
        
        if (html.isBlank()) {
            Log.e("MANGANATO", "HTML is blank for URL: $url")
            return emptyList()
        }

        val doc = Jsoup.parse(html)
        Log.d("MANGANATO", "Jsoup parsed HTML, length: ${html.length}")
        
        // Handle potential redirect to 'manganato.com' vs 'readmanganato.com'
        val items = doc.select("div.content-genres-item, div.search-story-item")
        Log.e("MANGANATO", "Found ${items.size} items")

        return items.map { el ->
            val link = el.selectFirst("a.genres-item-img, a.item-img")?.attr("href")
            val title = el.selectFirst("a.genres-item-name, a.item-title")?.text().orEmpty()
            val cover = el.selectFirst("img")?.attr("src").orEmpty()

            SourceManga(
                url = absUrl(link),
                title = title,
                coverUrl = cover
            )
        }
    }

    override suspend fun searchManga(query: String, page: Int): List<SourceManga> {
        // Manganato search ignores page in URL, but we keep param for API compatibility
        val safeQuery = query.trim().replace(" ", "_")
        val url = "$base/search/story/$safeQuery"
        val html = engine.loadHtml(url)
        if (html.isBlank()) return emptyList()

        val doc = Jsoup.parse(html)

        // Items: div.search-story-item
        return doc.select("div.search-story-item").map { el ->
            val link = el.selectFirst("a.item-img")?.attr("href")
            val title = el.selectFirst("a.item-title")?.text().orEmpty()
            val cover = el.selectFirst("img")?.attr("src").orEmpty()

            SourceManga(
                url = absUrl(link),
                title = title,
                coverUrl = cover
            )
        }
    }

    override suspend fun getMangaDetails(url: String): SourceManga {
        val html = engine.loadHtml(url)
        val doc = Jsoup.parse(html)

        val title = doc.selectFirst("h1.story-info-title")?.text().orEmpty()
        val cover = doc.selectFirst("span.info-image img")?.attr("src").orEmpty()
        val desc = doc.selectFirst("div.panel-story-info-description")?.ownText().orEmpty()

        return SourceManga(
            url = url,
            title = title,
            coverUrl = cover,
            description = desc
        )
    }

    override suspend fun getChapterList(mangaUrl: String): List<SourceChapter> {
        val html = engine.loadHtml(mangaUrl)
        val doc = Jsoup.parse(html)

        // Chapters: div.panel-story-chapter-list li.a-h
        val chapters = doc.select("div.panel-story-chapter-list li.a-h")
        return chapters.mapIndexed { index, el ->
            val link = el.selectFirst("a.chapter-name")?.attr("href")
            val title = el.selectFirst("a.chapter-name")?.text().orEmpty()

            SourceChapter(
                url = absUrl(link),
                title = title,
                number = (chapters.size - index).toFloat() // newest first on site
            )
        }
    }

    override suspend fun getPageList(chapterUrl: String): List<SourcePage> {
        val html = engine.loadHtml(chapterUrl)
        val doc = Jsoup.parse(html)

        // Reader images: div.container-chapter-reader img
        val imgs = doc.select("div.container-chapter-reader img")
        return imgs.mapIndexed { index, el ->
            val src = el.attr("src")
            SourcePage(
                index = index,
                imageUrl = src
            )
        }
    }
}
