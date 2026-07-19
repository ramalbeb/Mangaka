package com.mangaka.domain.model

data class Manga(
    val id: Long = 0L,
    val url: String,
    val title: String,
    val coverUrl: String,
    val description: String = "",
    val author: String = "",
    val artist: String = "",
    val genres: List<String> = emptyList(),
    val status: String = "",
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = false,
    val lastRead: Long = 0L,
    val lastUpdated: Long = 0L,
    val unreadCount: Int = 0
)
