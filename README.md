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

Create an instance with context, socket server url and device token.

```java
SCMessaging scMessaging = new SCMessaging(this, "http://192.168.8.200:3000", "user_token");
```

Imlement the `onMessageData` method to listen the push messages.

```java
scMessaging.setListener(new SCMessaging.Listener() {
    @Override
    public void onMessageData(String data) {
        Log.d("onMessageData", data);
    }
});
```

Finally call the connect method to start the socket connection

```java
scMessaging.connect();
```

### Use case

### Demonstration
[![Screenshot](/screenshots/1.png)](/screenshots/1.png)

Test the sample app that I made for you: [TFLite-Image-v1.0.apk](https://drive.google.com/file/d/1YFNNx25bvUhahTkaL_TrRV3MLaQCXedT/view?usp=sharing)

<hr/>

Made with ❤️ by <b>Aslam Anver</b>
