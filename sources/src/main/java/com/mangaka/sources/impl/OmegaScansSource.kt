package com.mangaka.sources.impl

import com.mangaka.engine.WebViewEngine
import com.mangaka.sources.BaseSource
import com.mangaka.sources.models.SourceChapter
import com.mangaka.sources.models.SourceManga
import com.mangaka.sources.models.SourcePage
import org.jsoup.Jsoup

class OmegaScansSource(
    private val engine: WebViewEngine
) : BaseSource(
    id = 3002L,
    name = "OmegaScans",
    lang = "en",
    isWebView = true
) {

    private val base = "https://omegascans.org"

    override suspend fun getPopularManga(page: Int): List<SourceManga> {
        val url = "$base/manga/?page=$page"
        val html = engine.loadHtml(url)

        val doc = Jsoup.parse(html)

        return doc.select("div.bs").map { el ->
            val link = el.selectFirst("a")?.attr("href").orEmpty()
            val title = el.selectFirst(".tt")?.text().orEmpty()
            val cover = el.selectFirst("img")?.attr("src").orEmpty()

            SourceManga(
                url = link,
                title = title,
                coverUrl = cover
            )
        }
    }

    override suspend fun searchManga(query: String, page: Int): List<SourceManga> {
        val url = "$base/?s=$query"
        val html = engine.loadHtml(url)

        val doc = Jsoup.parse(html)

        return doc.select("div.bs").map { el ->
            val link = el.selectFirst("a")?.attr("href").orEmpty()
            val title = el.selectFirst(".tt")?.text().orEmpty()
            val cover = el.selectFirst("img")?.attr("src").orEmpty()

            SourceManga(
                url = link,
                title = title,
                coverUrl = cover
            )
        }
    }

    override suspend fun getMangaDetails(mangaUrl: String): SourceManga {
        val html = engine.loadHtml(mangaUrl)
        val doc = Jsoup.parse(html)

        return SourceManga(
            url = mangaUrl,
            title = doc.selectFirst("h1.entry-title")?.text().orEmpty(),
            coverUrl = doc.selectFirst(".thumb img")?.attr("src").orEmpty(),
            description = doc.selectFirst(".entry-content p")?.text().orEmpty()
        )
    }

    override suspend fun getChapterList(mangaUrl: String): List<SourceChapter> {
        val html = engine.loadHtml(mangaUrl)
        val doc = Jsoup.parse(html)

        return doc.select("li.wp-manga-chapter").mapIndexed { index, el ->
            SourceChapter(
                url = el.selectFirst("a")?.attr("href").orEmpty(),
                title = el.text(),
                number = (index + 1).toFloat()
            )
        }
    }

    override suspend fun getPageList(chapterUrl: String): List<SourcePage> {
        val html = engine.loadHtml(chapterUrl)
        val doc = Jsoup.parse(html)

        return doc.select("img.wp-manga-chapter-img").mapIndexed { index, el ->
            SourcePage(
                index = index,
                imageUrl = el.attr("src")
            )
        }
    }
}
