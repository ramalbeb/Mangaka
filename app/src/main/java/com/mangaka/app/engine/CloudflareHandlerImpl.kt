/*
package com.mangaka.app.engine

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.mangaka.engine.CloudflareActivity
import com.mangaka.engine.CloudflareHandler

class CloudflareHandlerImpl(
    private val activity: FragmentActivity,
    private val onFullscreenToggle: (Boolean) -> Unit
) : CloudflareHandler {

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var pendingCallback: ((Boolean) -> Unit)? = null

    fun register() {
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            // Hide fullscreen WebView after CloudflareActivity closes
            onFullscreenToggle(false)

            val success = result.resultCode == Activity.RESULT_OK
            pendingCallback?.invoke(success)
            pendingCallback = null
        }
    }

    override fun solve(url: String, callback: (Boolean) -> Unit) {
        pendingCallback = callback

        // Show fullscreen WebView before launching CloudflareActivity
        onFullscreenToggle(true)

        val intent = Intent(activity, CloudflareActivity::class.java).apply {
            putExtra("url", url)
        }

        launcher.launch(intent)
    }
}
*/
