package com.mangaka.engine

import android.webkit.JavascriptInterface

class JsBridge(
    private val onHtmlReceived: (String) -> Unit
) {
    @JavascriptInterface
    fun returnHtml(html: String) {
        onHtmlReceived(html)
    }
}
