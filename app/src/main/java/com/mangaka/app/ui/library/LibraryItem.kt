package com.mangaka.app.ui.library

data class LibraryItem(
    val id: Long,
    val title: String,
    val coverUrl: String,
    val category: String,

    // Sorting fields
    val lastRead: Long = 0L,
    val lastUpdated: Long = 0L,
    val dateAdded: Long = 0L,

    // Filter fields
    val isDownloaded: Boolean = false,
    val unreadCount: Int = 0,
    val isCompleted: Boolean = false,
    val isFavorite: Boolean = false
)
