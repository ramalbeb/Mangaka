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

class CloudflareWebViewEngine(
    context: Context
) {

    private val cookieManager = CookieManager.getInstance()

    // Persistent WebView (Kotatsu‑style)
    private val webView: WebView = WebView(context).apply {
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.loadsImagesAutomatically = true
        settings.blockNetworkImage = false
        settings.blockNetworkLoads = false
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.userAgentString =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/120.0.0.0 Safari/537.36"

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    // Expose to attach in Activity
    val view: WebView get() = webView

    /**
     * Loads URL in the persistent WebView.
     * User solves Cloudflare inside this WebView.
     * Once the real page is loaded, we extract HTML via JS.
     */
    suspend fun loadHtml(url: String): String =
        withContext(Dispatchers.Main) {
            Log.e("CF_ENGINE", "loadHtml() for $url")

            suspendCancellableCoroutine { cont ->

                cont.invokeOnCancellation {
                    try {
                        webView.stopLoading()
                    } catch (_: Throwable) {
                    }
                }

                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, finishedUrl: String?) {
                        if (finishedUrl == null) return

                        Log.e("CF_ENGINE", "onPageFinished: $finishedUrl")

                        // If still Cloudflare, just wait — user will solve it.
                        // We only extract HTML when user has solved and real page is shown.
                        view?.evaluateJavascript(
                            """
                            (function() {
                                try {
                                    var html = document.documentElement.outerHTML || "";
                                    return html;
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

                            Log.e("MangaLekHTML", html.take(200))

                            if (!cont.isCompleted) {
                                cont.resume(html)
                            }
                        }
                    }
                }

                val headers = mapOf("Accept-Language" to "en-US,en;q=0.9")
                webView.loadUrl(url, headers)
            }
        }
}
