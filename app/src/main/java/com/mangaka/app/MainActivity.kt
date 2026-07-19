package com.mangaka.app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mangaka.engine.WebViewEngine
import com.mangaka.sources.SourceManager
import com.mangaka.sources.impl.MangaLekSource
import com.mangaka.sources.impl.MangaPillSource
import com.mangaka.sources.impl.ManganatoSource
import com.mangaka.sources.impl.OmegaScansSource

class MainActivity : AppCompatActivity() {

    lateinit var engine: WebViewEngine

    private lateinit var hiddenContainer: FrameLayout
    private lateinit var fullscreenContainer: FrameLayout
    private lateinit var persistentWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hiddenContainer = findViewById(R.id.hidden_webview_container)
        fullscreenContainer = findViewById(R.id.fullscreen_webview_container)

        // Create ONE persistent WebView
        persistentWebView = WebView(this)
        persistentWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        hiddenContainer.addView(persistentWebView)

        // Engine uses the SAME WebView for everything
        engine = WebViewEngine(persistentWebView)


        /*Here is the source
        registration for any other sources*/


        //Register Manganato
        SourceManager.register(ManganatoSource(engine))



        // Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as androidx.navigation.fragment.NavHostFragment

        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
    }

    private fun showWebViewFullscreen() {
        if (persistentWebView.parent === hiddenContainer) {
            hiddenContainer.removeView(persistentWebView)
        }
        if (persistentWebView.parent == null) {
            fullscreenContainer.addView(persistentWebView)
        }
        fullscreenContainer.visibility = View.VISIBLE
    }

    private fun hideWebViewFullscreen() {
        if (persistentWebView.parent === fullscreenContainer) {
            fullscreenContainer.removeView(persistentWebView)
        }
        if (persistentWebView.parent == null) {
            hiddenContainer.addView(persistentWebView)
        }
        fullscreenContainer.visibility = View.GONE
    }

    /**
     * Called when Cloudflare is detected.
     * Shows the persistent WebView fullscreen and loads the Cloudflare URL.
     * User solves it, then presses back to return.
     */
    fun showCloudflare(url: String) {
        Log.e("CF", "showCloudflare $url")
        showWebViewFullscreen()
        engine.loadUrlDirect(url)
    }

    override fun onBackPressed() {
        // If WebView is currently fullscreen (Cloudflare solving), hide it instead of exiting app
        if (fullscreenContainer.visibility == View.VISIBLE) {
            hideWebViewFullscreen()
        } else {
            super.onBackPressed()
        }
    }
}
