package com.mangaka.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manga")
data class MangaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val url: String,
    val title: String,
    val coverUrl: String,
    val description: String,
    val author: String,
    val artist: String,
    val genres: String, // stored as CSV
    val status: String,
    val isFavorite: Boolean,
    val isDownloaded: Boolean,
    val lastRead: Long,
    val lastUpdated: Long,
    val unreadCount: Int
)
