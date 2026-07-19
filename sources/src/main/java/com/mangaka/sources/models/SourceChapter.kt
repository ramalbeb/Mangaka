package com.mangaka.sources.models

data class SourceChapter(
    val url: String,
    val title: String,
    val number: Float,
    val dateUpload: Long = 0L
)
