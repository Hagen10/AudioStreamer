# README

## To do
* figure out the assets location since it keeps complaining about not being able to find the ssl certs whatever
* Not we're also relying on gstreamer-webrtc-1.0 gstreamer-sdp-1.0 gstreamer-video-1.0. Check if this is what
fixed the "no element found openlessrc" or if in fact using $(GSTREAMER_PLUGINS_CODECS) is enough

### Troubleshooting
* "GSTREAMER_JAVA_SRC_DIR=src/main/java" has to be set to the parent directory of your MainActivity directory.
In this case it's com.test.audiostreamer.

* GStreamer has to be initialized in OnCreate with GStreamer.init(this), for it to load all the GStreamer
elements. That's why we kept getting "no element found" errors...