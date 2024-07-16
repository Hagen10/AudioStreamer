# README

## To do
* figure out the assets location since it keeps complaining about not being able to find the ssl certs whatever
* Now we're also relying on gstreamer-webrtc-1.0 gstreamer-sdp-1.0 gstreamer-video-1.0. Check if this is what
fixed the "no element found openlessrc" or if in fact using $(GSTREAMER_PLUGINS_CODECS) is enough
* In terms of refactoring, maybe draw inspiration from: https://github.com/pexip/gstreamer/blob/4a40ed06ee911690f64af0167d0e0812a7f25664/subprojects/gst-examples/webrtc/android/app/src/main/java/org/freedesktop/gstreamer/webrtc/WebRTC.java

## Testing
Use this on the receiver side if port is 5000:
gst-launch-1.0 -v udpsrc port=5000 caps="application/x-rtp, media=(string)audio, payload=(int)96, encoding-name=(string)OPUS" ! rtpopusdepay ! opusdec ! audioconvert ! audioresample ! autoaudiosink

### Troubleshooting
* "GSTREAMER_JAVA_SRC_DIR=src/main/java" has to be set to the parent directory of your MainActivity directory.
In this case it's com.test.audiostreamer.

* GStreamer has to be initialized in OnCreate with GStreamer.init(this), for it to load all the GStreamer
elements. That's why we kept getting "no element found" errors...