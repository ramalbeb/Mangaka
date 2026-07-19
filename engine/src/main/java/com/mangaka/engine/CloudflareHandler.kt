package com.mangaka.engine

interface CloudflareHandler {
    fun solve(url: String, callback: (Boolean) -> Unit)
}
