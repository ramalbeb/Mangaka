package com.mangaka.sources.models

data class SourceManga(
    val url: String,
    val title: String,
    val coverUrl: String,
    val description: String = "",
    val author: String = "",
    val artist: String = "",
    val genres: List<String> = emptyList(),
    val status: String = ""
)
