package com.test.audiostreamer

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import org.freedesktop.gstreamer.GStreamer

class MainActivity : ComponentActivity() {
    private external fun nativeInit(ipAddress : String, port : String) // Initialize native code, build pipeline, etc
    private external fun nativeFinalize() // Destroy pipeline and shutdown native code
    private external fun nativePlay() // Set pipeline to PLAYING
    private external fun nativePause() // Set pipeline to PAUSED

    private val native_custom_data: Long = 0 // Native code will use this to keep private data

    private val IPREGEX : Regex = Regex("^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})){3}$")
    private val PORTREGEX = Regex("^[1-9][0-9]*$")

    private var is_streaming : Boolean = false

    private var is_configured : Boolean = false
    companion object {
        @JvmStatic
        private external fun nativeClassInit(): Boolean // Initialize native class: cache Method IDs for callbacks

        init {
            System.loadLibrary("gstreamer_android")
            System.loadLibrary("tutorial-2")
            nativeClassInit()
        }
    }

    // Called from native code. This sets the content of the TextView from the UI thread.
    private fun setMessage(message: String) {
//        val tv = findViewById<View>(R.id.textview_message) as TextView
//        runOnUiThread { tv.text = message }
    }

    // Called from native code. Native code calls this once it has created its pipeline and
    // the main loop is running, so it is ready to accept commands.
    private fun onGStreamerInitialized() {
//        Log.i("GStreamer", "Gst initialized. Restoring state, playing:$is_playing_desired")
//        // Restore previous playing state
//        if (is_playing_desired) {
//            nativePlay()
//        } else {
//            nativePause()
//        }
//
//        // Re-enable buttons, now that GStreamer is initialized
//        val activity: Activity = this
//        runOnUiThread {
//            activity.findViewById<View>(R.id.button_play).isEnabled = true
//            activity.findViewById<View>(R.id.button_stop).isEnabled = true
//        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize GStreamer and warn if it fails
        try {
            GStreamer.init(this)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setContentView(R.layout.layout)

        findViewById<Button>(R.id.configure_button).setOnClickListener {
            var ipAddress = findViewById<EditText>(R.id.ip_address).text.toString()
            var port = findViewById<EditText>(R.id.port).text.toString()

            if (is_configured)
                Log.e(
                    "MainActivity",
                    String.format("Streamer was already configured, restart app")
                )
                else {
                if (IPREGEX.matches(ipAddress) and PORTREGEX.matches(port)) {
                    Log.w(
                        "MainActivity",
                        String.format("Setting up streamer with ip %s and port %s", ipAddress, port)
                    )
                    nativeInit(ipAddress, port)

                    is_configured = true
                } else
                    Log.w(
                        "MainActivity",
                        String.format("IP OR PORT WAS NOT VALID %s - %s", ipAddress, port)
                    )
            }
        }

        findViewById<Button>(R.id.stream_button).setOnClickListener {
            if (is_streaming) {
                // Stopping streaming
                nativePause()
                findViewById<Button>(R.id.stream_button).text = "Start Streaming"
                is_streaming = false
            } else {
                if (is_configured) {
                    nativePlay()
                    is_streaming = true
                    findViewById<Button>(R.id.stream_button).text = "Stop Streaming"
                } else
                    Log.w("MainActivity", String.format("Streamer is not configured!"))
            }
        }

        // Check Permissions Now
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf<String>(Manifest.permission.RECORD_AUDIO),
            1
        )
    }

    override fun onDestroy() {
        nativeFinalize()
        super.onDestroy()
    }
}
