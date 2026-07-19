package com.mangaka.app.ui.downloads

import android.content.Context
import com.mangaka.data.download.DownloadStorage
import com.mangaka.sources.SourceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

object DownloadRepository {

    private val _downloads = MutableStateFlow<List<DownloadItem>>(emptyList())
    val downloads: StateFlow<List<DownloadItem>> = _downloads

    suspend fun startDownload(context: Context, item: DownloadItem) {
        if (isDownloaded(context, item)) return

        _downloads.value += item

        withContext(Dispatchers.IO) {
            val source = SourceManager.get(item.sourceId)
                ?: throw IllegalStateException("Source not found: ${item.sourceId}")

            val pages = source.getPageList(item.chapterUrl)
            if (pages.isEmpty()) {
                item.isCompleted = true
                update(item)
                return@withContext
            }

            val chapterDir = DownloadStorage.getChapterDir(
                context, item.sourceId, item.chapterUrl
            )

            pages.forEachIndexed { index, page ->
                val outFile = DownloadStorage.getPageFile(chapterDir, index)

                if (!outFile.exists()) {
                    DownloadStorage.downloadImageToFile(page.imageUrl, outFile)
                }

                item.progress = ((index + 1) * 100) / pages.size
                update(item)
            }

            item.isCompleted = true
            update(item)
        }
    }

    private fun update(item: DownloadItem) {
        _downloads.value = _downloads.value.map {
            if (it.chapterUrl == item.chapterUrl && it.sourceId == item.sourceId) item else it
        }
    }

    fun isDownloaded(context: Context, item: DownloadItem): Boolean {
        return DownloadStorage.fileExists(context, item.sourceId, item.chapterUrl)
    }
}
