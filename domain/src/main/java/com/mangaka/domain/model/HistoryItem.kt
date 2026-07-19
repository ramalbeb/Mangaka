package com.mangaka.domain.model

data class HistoryItem(
    val mangaTitle: String,
    val coverUrl: String,
    val sourceId: Long,
    val mangaUrl: String,
    val chapterTitle: String,
    val chapterUrl: String,
    val lastPage: Int,
    val timestamp: Long = System.currentTimeMillis()
)