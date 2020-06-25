# SCM - SCMessaging

![https://i.imgur.com/wyb0Vv7.png](https://i.imgur.com/wyb0Vv7.png)

[ ![Download](https://api.bintray.com/packages/aslam/android/tflite-image/images/download.svg) ](https://bintray.com/aslam/android/tflite-image) [![](https://jitpack.io/v/aslamanver/tflite-image.svg)](https://jitpack.io/#aslamanver/tflite-image) [![Build Status](https://travis-ci.com/aslamanver/tflite-image.svg?branch=master)](https://travis-ci.com/aslamanver/tflite-image)

#### SCM - Socket Cloud Messaging for Android | Socket.IO
Android push notification service based on [io-session-handler](https://github.com/aslamanver/io-session-handler)

Building an own Android push messaging service to receive data messages in the background in real-time and alternate for Firebase Cloud Messaging. 

### Initialization

Add the below dependency into your module level `build.gradle` file

```gradle
implementation 'com.aslam:scm:+'
```

Make sure you have set `usesCleartextTraffic` to true in `AndroidManifest.xml` file
```xml
<application
    ...
    android:usesCleartextTraffic="true">
```

### Simple Usage

You need to pass the model file, label text and the model type.

```java
SCMessaging scMessaging = new SCMessaging(this, "https://192.168.8.200:3000", "user_id");
scMessaging.setListener(new SCMessaging.Listener() {
    @Override
    public void onMessageData(String data) {
        Log.d("onMessageData", data);
    }
});
scMessaging.connect();
```

### Use case

```java
TFLiteImage tfLite = TFLiteImage.getInstance(this, "inception_quant.tflite", "labels.txt", TFLiteImage.TYPE.QUANT);
List<Map<String, String>> results = tfLite.predictImage(binding.imgView);

for (Map<String, String> map : results) {
    Log.e("RESULT", map.get("LABEL") + " - " + map.get("CONFIDENCE"));
}
```

Result

```java
map.get("LABEL");
map.get("CONFIDENCE");
```

Sunglass - 99% <br/>
Glass - 85% <br/>
Jeans - 70% <br/>

### Demonstration
[![Screenshot](/screenshots/1.png)](/screenshots/1.png)

Test the sample app that I made for you: [TFLite-Image-v1.0.apk](https://drive.google.com/file/d/1YFNNx25bvUhahTkaL_TrRV3MLaQCXedT/view?usp=sharing)

<hr/>

Made with ❤️ by <b>Aslam Anver</b>
