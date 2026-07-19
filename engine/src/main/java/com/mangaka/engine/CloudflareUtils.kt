package com.mangaka.engine

object CloudflareUtils {

    fun isCloudflareChallenge(html: String): Boolean {
        if (html.isBlank()) return true

        val lower = html.lowercase()

        return when {
            "just a moment" in lower -> true
            "checking your browser" in lower -> true
            "cf-browser-verification" in lower -> true
            "cf-challenge" in lower -> true
            "challenges.cloudflare.com" in lower -> true
            "turnstile" in lower -> true
            "cf-turnstile" in lower -> true
            "attention required" in lower -> true
            else -> false
        }
    }
}
