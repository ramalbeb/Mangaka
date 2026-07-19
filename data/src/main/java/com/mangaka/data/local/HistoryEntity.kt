package com.mangaka.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val mangaTitle: String,
    val coverUrl: String,
    val sourceId: Long,
    val mangaUrl: String,
    val chapterTitle: String,
    val chapterUrl: String,
    val lastPage: Int,
    val timestamp: Long
)
