# SCM - SCMessaging

![https://i.imgur.com/wyb0Vv7.png](https://i.imgur.com/wyb0Vv7.png)

[ ![Download](https://api.bintray.com/packages/aslam/android/scm/images/download.svg) ](https://bintray.com/aslam/android/scm) [![](https://jitpack.io/v/aslamanver/scm.svg)](https://jitpack.io/#aslamanver/scm) [![Build Status](https://travis-ci.com/aslamanver/scm.svg?branch=master)](https://travis-ci.com/aslamanver/scm)

#### SCM - Socket Cloud Messaging for Android | Socket.IO
Android push notification service based on [io-session-handler](https://aslamanver.github.io/io-session-handler)

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

Create a `SCMessaging` instance with context, socket server url and device token.

```java
SCMessaging scMessaging = new SCMessaging(this, "http://192.168.8.200:3000", "user_token");
```

Implement `onMessageData` method to listen the server push messages.

```java
scMessaging.setListener(new SCMessaging.Listener() {
    @Override
    public void onMessageData(String data) {
        Log.d("onMessageData", data);
    }
});
```

Finally call the connect method to start the socket connection.

```java
scMessaging.connect();
```

### Advanced Usage

Override all the methods in `SCMessaging.Listener()` to notify the program from more events as below.

```java
scMessaging.setListener(new SCMessaging.Listener() {

    @Override
    public void onMessageData(String data) {

    }

    @Override
    public void onConnect(String serverURL) {

    }

    @Override
    public void onDisconnect(String serverURL) {

    }

    @Override
    public void onConnectError(Exception ex) {

    }
});
```

### More Options

This is the constructor for passing more options to the Socket.IO engine, the options variable should be an `IO.Options()` object, if it's not included in your project, you can add the library in the `build.gradle` file.

```java
IO.Options options = new IO.Options();
options.reconnection = false;
SCMessaging scMessaging = new SCMessaging(this, "http://192.168.8.200:3000", "user_token", options);
```

```gradle
implementation ('io.socket:socket.io-client:1.0.0') {
    exclude group: 'org.json', module: 'json'
}
```

### Demonstration
[![Screenshot](/screenshots/2.gif)](/screenshots/2.gif)

Test the sample app that I made for you: [SCM-Demo-v1.0.1.apk](https://drive.google.com/file/d/1BYMLQG4nX-d6qAYG_zSeEMME0ZxVXneG/view?usp=sharing)

<hr/>

Made with ❤️ by <b>Aslam Anver</b>
