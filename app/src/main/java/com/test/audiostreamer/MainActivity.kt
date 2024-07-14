package com.test.audiostreamer

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import org.freedesktop.gstreamer.GStreamer

class MainActivity : ComponentActivity() {
    private external fun nativeInit() // Initialize native code, build pipeline, etc
    private external fun nativeFinalize() // Destroy pipeline and shutdown native code
    private external fun nativePlay() // Set pipeline to PLAYING
    private external fun nativePause() // Set pipeline to PAUSED

    private val native_custom_data: Long = 0 // Native code will use this to keep private data
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

        findViewById<Button>(R.id.stream_button).setOnClickListener {
            nativePlay()
        }

        findViewById<Button>(R.id.stop_button).setOnClickListener {
            nativePause()
        }


        nativeInit()

//
//        setContent {
//            AudioStreamerTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
    }

    override fun onDestroy() {
        nativeFinalize()
        super.onDestroy()
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    AudioStreamerTheme {
//        Greeting("Android")
//    }
//}