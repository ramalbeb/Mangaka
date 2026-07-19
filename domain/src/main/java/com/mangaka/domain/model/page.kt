package com.mangaka.domain.model

data class Page(
    val index: Int,
    val imageUrl: String,
    val chapterId: Long = 0L
)
