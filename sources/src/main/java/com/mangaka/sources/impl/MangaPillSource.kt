package com.mangaka.sources.impl

import com.mangaka.engine.WebViewEngine
import com.mangaka.sources.BaseSource
import com.mangaka.sources.models.SourceChapter
import com.mangaka.sources.models.SourceManga
import com.mangaka.sources.models.SourcePage
import org.jsoup.Jsoup

class MangaPillSource(
    private val engine: WebViewEngine
) : BaseSource(
    id = 3001L,
    name = "MangaPill",
    lang = "en",
    isWebView = true
    ) {

    private val base = "https://mangapill.com"

    override suspend fun getPopularManga(page: Int): List<SourceManga> {
        val url = "$base/manga?page=$page"
        val html = engine.loadHtml(url)
        if (html.isBlank()) return emptyList()

        val doc = Jsoup.parse(html)

        // Adjust selectors if MangaPill changes layout
        return doc.select("a.manga-item").map { el ->
            val href = el.attr("href")
            val title = el.selectFirst(".manga-title")?.text().orEmpty()
            val img = el.selectFirst("img")?.attr("src").orEmpty()

            SourceManga(
                url = if (href.startsWith("http")) href else base + href,
                title = title,
                coverUrl = img
            )
        }
    }

    override suspend fun searchManga(query: String, page: Int): List<SourceManga> {
        val url = "$base/search?q=$query&page=$page"
        val html = engine.loadHtml(url)
        if (html.isBlank()) return emptyList()

        val doc = Jsoup.parse(html)

        return doc.select("a.manga-item").map { el ->
            val href = el.attr("href")
            val title = el.selectFirst(".manga-title")?.text().orEmpty()
            val img = el.selectFirst("img")?.attr("src").orEmpty()

            SourceManga(
                url = if (href.startsWith("http")) href else base + href,
                title = title,
                coverUrl = img
            )
        }
    }

    override suspend fun getMangaDetails(url: String): SourceManga {
        val html = engine.loadHtml(url)
        val doc = Jsoup.parse(html)

        val title = doc.selectFirst("h1")?.text().orEmpty()
        val cover = doc.selectFirst("img[alt=\"Cover\"]")?.attr("src").orEmpty()
        val desc = doc.selectFirst("p.text-gray-300")?.text().orEmpty()

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

        val chapters = doc.select("a.chapter-link")
        return chapters.mapIndexed { index, el ->
            val href = el.attr("href")
            val title = el.text().trim()

            SourceChapter(
                url = if (href.startsWith("http")) href else base + href,
                title = title,
                number = (index + 1).toFloat()
            )
        }
    }

    override suspend fun getPageList(chapterUrl: String): List<SourcePage> {
        val html = engine.loadHtml(chapterUrl)
        val doc = Jsoup.parse(html)

        val imgs = doc.select("img.reader-image")
        return imgs.mapIndexed { index, el ->
            val src = el.attr("src")
            SourcePage(
                index = index,
                imageUrl = src
            )
        }
    }
}
