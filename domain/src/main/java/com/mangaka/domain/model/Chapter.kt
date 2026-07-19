package com.mangaka.domain.model

data class Chapter(
    val id: Long = 0L,
    val mangaId: Long = 0L,
    val url: String,
    val title: String,
    val number: Float,
    val dateUpload: Long = 0L,
    val read: Boolean = false,
    val lastPageRead: Int = 0,
    val pageCount: Int = 0,
    val isBookmarked: Boolean = false
)
