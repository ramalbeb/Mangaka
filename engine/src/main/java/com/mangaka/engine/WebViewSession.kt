/*
package com.mangaka.engine

import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class WebViewSession(context: Context) {

    private val cookieManager = CookieManager.getInstance()

    val view: WebView = WebView(context).apply {
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.loadsImagesAutomatically = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.userAgentString =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/120.0.0.0 Safari/537.36"

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

        setBackgroundColor(0x00000000)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    suspend fun loadHtml(url: String): String =
        withContext(Dispatchers.Main) {
            suspendCancellableCoroutine { cont ->

                view.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(v: WebView?, finishedUrl: String?) {
                        v?.evaluateJavascript(
                            """
                            (function() {
                                try {
                                    return document.documentElement.outerHTML;
                                } catch(e) {
                                    return "";
                                }
                            })();
                            """.trimIndent()
                        ) { result ->
                            val html = result
                                ?.removePrefix("\"")
                                ?.removeSuffix("\"")
                                ?.replace("\\n", "\n")
                                ?.replace("\\\"", "\"")
                                ?: ""

                            Log.e("HTML", html.take(200))

                            if (!cont.isCompleted) cont.resume(html)
                        }
                    }
                }

                view.loadUrl(url)
            }
        }
}
*/
