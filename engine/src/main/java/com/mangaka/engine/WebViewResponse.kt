package com.mangaka.engine

data class WebViewResponse(
    val url: String,
    val html: String,
    val cookies: String
)
