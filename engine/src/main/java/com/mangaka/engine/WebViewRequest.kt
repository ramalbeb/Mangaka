package com.mangaka.engine

data class WebViewRequest(
    val url: String,
    val headers: Map<String, String> = emptyMap(),
    val js: String? = null, // optional JS to inject
)
