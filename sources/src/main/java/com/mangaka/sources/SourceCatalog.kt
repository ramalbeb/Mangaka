package com.mangaka.sources

import com.mangaka.sources.mangadex.MangaDexSource

object SourceCatalog {

    private val staticSources: List<BaseSource> = listOf(
        MangaDexSource() // only static source
    )

    fun getStaticSources(): List<BaseSource> = staticSources

    fun getStaticSourceById(id: Long): BaseSource? =
        staticSources.find { it.id == id }
}
