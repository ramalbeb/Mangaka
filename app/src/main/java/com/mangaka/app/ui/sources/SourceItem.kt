package com.mangaka.app.ui.sources

data class SourceItem(
    val id: Long,
    val name: String,
    val iconUrl: String = "",
    val language: String
)
