# SCM - SCMessaging

![https://i.imgur.com/wyb0Vv7.png](https://i.imgur.com/wyb0Vv7.png)

[ ![Download](https://api.bintray.com/packages/aslam/android/tflite-image/images/download.svg) ](https://bintray.com/aslam/android/tflite-image) [![](https://jitpack.io/v/aslamanver/tflite-image.svg)](https://jitpack.io/#aslamanver/tflite-image) [![Build Status](https://travis-ci.com/aslamanver/tflite-image.svg?branch=master)](https://travis-ci.com/aslamanver/tflite-image)

#### SCM - Socket Cloud Messaging for Android | Socket.IO
Android push notification service based on [io-session-handler](https://github.com/aslamanver/io-session-handler)

Building an own Android push messaging service to receive data messages in the background in real-time and alternate for Firebase Cloud Messaging. 

### Initialization

Add the below dependency into your module level `build.gradle` file

```gradle
implementation 'com.aslam:tflite-image:+'
```

Make sure you have added no compress config for your model files
```gradle
android {
    ....
    aaptOptions {
        noCompress "tflite"
        noCompress "lite"
    }
}
```

### Simple Usage

You need to pass the model file, label text and the model type.

```java
TFLiteImage tfLite = TFLiteImage.getInstance(activity, "your_model_file.tflite", "labels.txt", TFLiteImage.TYPE.QUANT, IMG_DIM_SIZE);
List<Map<String, String>> results = tfLite.predictImage(image view or bitmap image);
```
> `IMG_DIM_SIZE` is 299 or 224 according to your model, you can visualize your model data to check `IMG_DIM_SIZE`.

Inception model types
```java
TFLiteImage.TYPE.QUANT
TFLiteImage.TYPE.FLOAT
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
