package com.mangaka.reader

import com.mangaka.domain.model.Page

data class ReaderState(
    val isLoading: Boolean = false,
    val pages: List<Page> = emptyList(),
    val error: String? = null
)
