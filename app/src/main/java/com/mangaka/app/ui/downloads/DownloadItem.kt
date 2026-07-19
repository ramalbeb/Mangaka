package com.mangaka.app.ui.downloads

data class DownloadItem(
    val mangaTitle: String,
    val chapterTitle: String,
    val sourceId: Long,
    val chapterUrl: String,
    var progress: Int = 0,
    var isCompleted: Boolean = false
)
