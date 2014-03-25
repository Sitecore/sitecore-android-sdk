Sitecore Mobile SDK 2.0 for Android
===================================

Sitecore Mobile SDK for Android, is a framework that is designed to help the developer produce Android based applications that use and serve content that is managed by Sitecore. 

This enables developers to rapidly develop Android applications (across multiple Android devices with differing screen sizes) that fully utilize the features of the device, for example, camera, location, accelerometer, and gestures. The SDK includes the following features:

 * Access to the camera and the photo library
 * QR code usage (scan and use QR Codes)
 * Accelerometer functions
 * Device information
 * Access to the built-in address book and easy creation of new contacts
 * Sending emails using the e-mail account
 * Social networks integration
 * Google Maps integration

You can also download a sample application that can be used as a starting point to understand the Mobile SDK for iOS and the features available. This project can be found in a corresponding [github repository][4]

Documentation, including installation and developer guides can be found on the [Sitecore Developer Network (SDN)][1]

## Framework Structure
The Sitecore Mobile SDK consists of three primary modules. They are

 * **sitecore-mobile-sdk** – a set of core classes that interact with the [Sitecore
Item Web Api][2] service.
 * **sitecore-mobile-javascript** – a library that allows using native features of iOS in mobile optimized Sitecore renderings. It contains an **enhanced web view** and **web plug-ins**.
 * **sitecore-mobile-ui** – UI extensions on top of **sitecore-mobile-sdk**. It contains Items Browser component for browsing through content tree with customizable UI.

The frameworks have some dependencies between each other. The dependencies are described in the following diagram:
![Framework Dependencies](https://raw.githubusercontent.com/Sitecore/sitecore-android-sdk/gh-pages/image/AndroidFrameworkDependencies.png)

## The SDK includes the following features:

### Java/Android API
 * Access data from Sitecore CMS
    * Authentication
    * CRUD operations on items
    * Access item fields and properties
    * Upload media items
    * Getting html rendering of an item
 * A local SQLite DB can be used with the SDK for easy access of Sitecore items offline

### JavaScript API
 * Show native alert
 * Accelerometer
 * Device information
 * Access to camera and media library
 * Share to social networks
 * Send emails
 * Access to the device address book
 * Open native Google Maps navigation
 * Create calendar events
 * Upload media files to the Sitecore media library

### UI components
 * Items Browser component for browsing through content tree with customizable UI
 * QR Code reader

## This repository contains:
 * Source code of the Sitecore SDK for Android framework
 * Binaries of the Sitecore SDK for Android framework in [releases section][5]

## Download

Sitecore SDK for Android framework can be downloaded from [releases section][5] or Maven repository:

```groovy
repositories {
    mavenCentral()
    maven { url "http://sitecore.github.io/sitecore-android-sdk/aar" }
}
dependencies {
    compile 'net.sitecore.android.sdk:sitecore-mobile-sdk:2.0'
    compile 'net.sitecore.android.sdk:sitecore-mobile-ui:2.0'
    
    compile 'net.sitecore.android.sdk:sitecore-mobile-javascript:2.0'
    compile 'com.android.support:support-v4:19.0.1'

    compile 'net.sitecore.android.sdk:sitecore-qr-reader:2.0'
    compile 'com.google.zxing:core:2.2'
}
```


## Further Information

 * [Product page on SDN][1]
 * [Sitecore Item Web API module page on SDN][2]
 * [Javadocs documentation][3]
 * [Sample App – SDK for Android][4]

# Licence
```
SITECORE SHARED SOURCE LICENSE
```

 [1]: http://sdn.sitecore.net/Products/Sitecore%20Mobile%20SDK/Sitecore%20Mobile%20SDK%20for%20Android.aspx
 [2]: http://sdn.sitecore.net/Products/Sitecore%20Item%20Web%20API.aspx
 [3]: http://sitecore.github.io/sitecore-android-sdk/javadoc/v2.0
 [4]: http://github.com/Sitecore/sitecore-android-sdk-sample
 [5]: https://github.com/Sitecore/sitecore-android-sdk/releases


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/Sitecore/sitecore-android-sdk/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

