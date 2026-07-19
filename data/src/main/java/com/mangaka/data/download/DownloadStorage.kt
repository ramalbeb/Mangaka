package com.mangaka.data.download

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

object DownloadStorage {

    private val client = OkHttpClient()

    fun getChapterDir(
        context: Context,
        sourceId: Long,
        chapterUrl: String
    ): File {
        val base = File(context.filesDir, "downloads")
        val sourceDir = File(base, sourceId.toString())
        val safeChapter = chapterUrl.hashCode().toString()
        val chapterDir = File(sourceDir, safeChapter)

        if (!chapterDir.exists()) chapterDir.mkdirs()
        return chapterDir
    }

    fun getPageFile(chapterDir: File, index: Int): File {
        val name = String.format("%03d.jpg", index + 1)
        return File(chapterDir, name)
    }

    fun fileExists(
        context: Context,
        sourceId: Long,
        chapterUrl: String
    ): Boolean {
        val dir = getChapterDir(context, sourceId, chapterUrl)
        return dir.exists() && dir.listFiles()?.isNotEmpty() == true
    }

    fun downloadImageToFile(url: String, outFile: File) {
        val request = Request.Builder().url(url).build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IllegalStateException("HTTP ${response.code}")

            val body = response.body ?: throw IllegalStateException("Empty body")

            FileOutputStream(outFile).use { fos ->
                body.byteStream().use { input ->
                    val buffer = ByteArray(8 * 1024)
                    while (true) {
                        val read = input.read(buffer)
                        if (read == -1) break
                        fos.write(buffer, 0, read)
                    }
                    fos.flush()
                }
            }
        }
    }
}
