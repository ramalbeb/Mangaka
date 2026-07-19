package com.mangaka.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapter")
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val mangaId: Long,
    val url: String,
    val title: String,
    val number: Float,
    val dateUpload: Long,
    val read: Boolean,
    val lastPageRead: Int,
    val pageCount: Int,
    val isBookmarked: Boolean
)
