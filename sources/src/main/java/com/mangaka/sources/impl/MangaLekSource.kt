package com.mangaka.sources.impl

import com.mangaka.engine.WebViewEngine
import com.mangaka.sources.BaseSource
import com.mangaka.sources.models.SourceChapter
import com.mangaka.sources.models.SourceManga
import com.mangaka.sources.models.SourcePage
import org.jsoup.Jsoup

class MangaLekSource(
    private val engine: WebViewEngine
) : BaseSource(
    id = 2001L,
    name = "MangaLek",
    lang = "ar",
    isWebView = true
) {

    private val base = "https://lekmanga.net"

    override suspend fun getPopularManga(page: Int): List<SourceManga> {
        val url = "$base/manga?page=$page"
        val html = engine.loadHtml(url)

        if (html == "__CLOUDFLARE__") {
            throw Exception("CLOUDFLARE")
        }
        if (html.isBlank()) return emptyList()

        val doc = Jsoup.parse(html)

        return doc.select("div.manga-card").map { el ->
            SourceManga(
                url = el.selectFirst("a")?.attr("href").orEmpty(),
                title = el.selectFirst(".manga-title")?.text().orEmpty(),
                coverUrl = el.selectFirst("img")?.attr("src").orEmpty()
            )
        }
    }

    override suspend fun searchManga(query: String, page: Int): List<SourceManga> {
        val url = "$base/?s=$query"
        val html = engine.loadHtml(url)

        if (html == "__CLOUDFLARE__") {
            throw Exception("CLOUDFLARE")
        }

        val doc = Jsoup.parse(html)

        return doc.select("div.manga-card").map { el ->
            SourceManga(
                url = el.selectFirst("a")?.attr("href").orEmpty(),
                title = el.selectFirst(".manga-title")?.text().orEmpty(),
                coverUrl = el.selectFirst("img")?.attr("src").orEmpty()
            )
        }
    }

    override suspend fun getMangaDetails(url: String): SourceManga {
        val html = engine.loadHtml(url)

        if (html == "__CLOUDFLARE__") {
            throw Exception("CLOUDFLARE")
        }

        val doc = Jsoup.parse(html)

        return SourceManga(
            url = url,
            title = doc.selectFirst("h1")?.text().orEmpty(),
            coverUrl = doc.selectFirst(".manga-cover img")?.attr("src").orEmpty(),
            description = doc.selectFirst(".description")?.text().orEmpty()
        )
    }

    override suspend fun getChapterList(mangaUrl: String): List<SourceChapter> {
        val html = engine.loadHtml(mangaUrl)

        if (html == "__CLOUDFLARE__") {
            throw Exception("CLOUDFLARE")
        }

        val doc = Jsoup.parse(html)

        return doc.select(".chapter-item").mapIndexed { index, el ->
            SourceChapter(
                url = el.attr("href"),
                title = el.text(),
                number = (index + 1).toFloat()
            )
        }
    }

    override suspend fun getPageList(chapterUrl: String): List<SourcePage> {
        val html = engine.loadHtml(chapterUrl)

        if (html == "__CLOUDFLARE__") {
            throw Exception("CLOUDFLARE")
        }

        val doc = Jsoup.parse(html)

        return doc.select(".page-img").mapIndexed { index, el ->
            SourcePage(
                index = index,
                imageUrl = el.attr("src")
            )
        }
    }
}
