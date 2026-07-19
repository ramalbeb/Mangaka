package com.mangaka.sources

abstract class HttpSource : Source {

    abstract override val baseUrl: String

    protected fun absoluteUrl(path: String): String {
        return if (path.startsWith("http")) path else baseUrl + path
    }
}
