package com.mangaka.sources.mangadex

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

object MangaDexApi {

    private val client = OkHttpClient()

    suspend fun request(url: String): String = withContext(Dispatchers.IO) {
        val req = Request.Builder().url(url).build()
        val res = client.newCall(req).execute()
        res.body?.string() ?: ""
    }

    suspend fun getPopularMangaEnglish(): String {
        return request("https://api.mangadex.org/manga?limit=20&order[followedCount]=desc")
    }
}
