# README


### Troubleshooting
* "GSTREAMER_JAVA_SRC_DIR=src/main/java" has to be set to the parent directory of your MainActivity directory.
In this case it's com.test.audiostreamer.

* GStreamer has to be initialized in OnCreate with GStreamer.init(this), for it to load all the GStreamer
elements. That's why we kept getting "no element found" errors...