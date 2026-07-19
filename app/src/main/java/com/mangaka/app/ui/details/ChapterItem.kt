package com.mangaka.app.ui.details

data class ChapterItem(
    val id: Long,
    val title: String,
    val number: Int,
    val url: String,
    var isDownloaded: Boolean = false,
    var isDownloading: Boolean = false,
    var progress: Int = 0
)
