package com.mangaka.sources

object SourceManager {

    private val staticSources = SourceCatalog.getStaticSources()
    private val dynamicSources = mutableListOf<BaseSource>()

    fun register(source: BaseSource) {
        dynamicSources += source
    }

    fun list(): List<BaseSource> = staticSources + dynamicSources

    fun get(id: Long): BaseSource? =
        staticSources.find { it.id == id }
            ?: dynamicSources.find { it.id == id }
}
