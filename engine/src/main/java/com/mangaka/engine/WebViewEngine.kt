package com.mangaka.engine

import android.net.Uri
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume

class WebViewEngine(
    private val webView: WebView
) {
    private var htmlCallback: ((String) -> Unit)? = null
    private var targetHost: String? = null

    init {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setSupportZoom(true)
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        
        settings.userAgentString = "Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36"

        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(webView, true)
        }

        webView.addJavascriptInterface(object {
            @android.webkit.JavascriptInterface
            fun returnHtml(html: String) {
                webView.post {
                    Log.d("ENGINE", "Bridge received HTML, length: ${html.length}")
                    htmlCallback?.invoke(html)
                }
            }
        }, "AndroidBridge")
    }

    suspend fun loadHtml(url: String, timeoutMillis: Long = 30000): String = withContext(Dispatchers.Main) {
        try {
            withTimeout(timeoutMillis) {
                suspendCancellableCoroutine { cont ->
                    
                    targetHost = Uri.parse(url).host
                    
                    htmlCallback = { html ->
                        if (html.length > 500) {
                            htmlCallback = null
                            if (!cont.isCompleted) cont.resume(html)
                        }
                    }

                    webView.webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            val newHost = request?.url?.host ?: ""
                            // Block any navigation to a DIFFERENT host (blocks ad redirects)
                            if (targetHost != null && !newHost.contains(targetHost!!)) {
                                Log.d("ENGINE", "Blocking redirect to: $newHost")
                                return true
                            }
                            return false
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            Log.d("ENGINE", "onPageFinished: $url")
                            injectPoller()
                        }
                        
                        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                            if (request?.isForMainFrame == true) {
                                Log.e("ENGINE", "WebView Error: ${error?.description}")
                            }
                        }
                    }

                    webView.webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, progress: Int) {
                            if (progress > 40) injectPoller()
                        }
                        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                            Log.d("ENGINE-JS", "[${consoleMessage?.messageLevel()}] ${consoleMessage?.message()}")
                            return true
                        }
                    }

                    Log.e("ENGINE", "Loading URL: $url")
                    webView.stopLoading()
                    webView.loadUrl(url)

                    cont.invokeOnCancellation {
                        htmlCallback = null
                        webView.stopLoading()
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            Log.e("ENGINE", "Timeout loading $url")
            ""
        } catch (e: Exception) {
            Log.e("ENGINE", "Unexpected error", e)
            ""
        }
    }

    private fun injectPoller() {
        webView.evaluateJavascript("""
            (function() {
                if (window.PollerStarted) return;
                window.PollerStarted = true;
                
                var attempt = 0;
                var poller = setInterval(function() {
                    attempt++;
                    var html = document.documentElement.outerHTML;
                    console.log('Poller check ' + attempt + ', state: ' + document.readyState + ', len: ' + html.length);
                    
                    if (html.length > 1000 || attempt > 30) {
                        if (window.AndroidBridge) {
                            window.AndroidBridge.returnHtml(html);
                            if (document.readyState === 'complete' || attempt > 30) {
                                clearInterval(poller);
                                window.PollerStarted = false;
                            }
                        }
                    }
                }, 1000);
            })();
        """.trimIndent(), null)
    }

    fun loadUrlDirect(url: String) {
        webView.loadUrl(url)
    }
}
