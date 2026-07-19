package com.mangaka.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page")
data class PageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val chapterId: Long,
    val index: Int,
    val imageUrl: String
)
