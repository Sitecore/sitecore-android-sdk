Change Log
==========

## Version 2.0 *(25-mar-2014)*

### Sitecore SDK
 * Sitecore SDK for Android is separated into three modules: 
   - sitecore-mobile-sdk - a set of core classes that interact with the Sitecore Item Web API service
   - sitecore-mobile-javascript - a library that allows using native features of Android in mobile optimized Sitecore renderings
   - sitecore-mobile-ui  - a set of UI extensions running on top of sitecore-mobile-sdk framework
 * Various API naming changes to be more consistent with Sitecore Mobile SDK for iOs
 * Added ability to specify media library path
 * Upload Media APIs updated to be more consistent with Items CRUD APIs
 * Added ability to specify default session parameters
 * Added handy methods:
   - Load children of specific item
   - Edit fields of specific item
   - Delete specific item
 * Gradle wrapper updated to v1.11
 * Android Support package updated to v.19.0.1
 
### Javascript API
 * Added ability to upload media items
 
### Sitecore UI Components:
 * ItemsBroserFragment separated into:
   - ItemsListBrowserFragment
   - ItemsGridBrowserFragment

## Version 1.1 *(17-dec-2013)*

### Javascript API
 * Calendar events

### Java/Android API
 * Gradle project model is used
 * Ability to save encryption key and to create session synchronously
 * Dependencies on external libraries removed
 * Added ability to remove cached items after succeeded response received
 * Added ability to set image download parameters to media items
 * Added *ItemsBrowserFragment* UI component for browsing through content tree
 * Added *ScItemsLoader* which allows asynchronous loading of database cache
 * Fixed #4 Update items/* uri updates all items
 * Fixed #5 UploadMediaRequestOptions set/get filename is not used

## Version 1.0 *(29-oct-2013)*
Initial release.

### JavaScript API
 * Show native alert
 * Toast messages
 * Accelerometer
 * Device information
 * Access to camera and media library
 * Share to social networks
 * Send emails
 * Access to the device address book
 * Open native Google Maps navigation

### Java/Android API
 * Access data from Sitecore CMS
   * Authentication
   * CRUD operations on items
   * Access item fields and properties
   * Upload media items
   * Getting html rendering of an item
 * Items & fields cache in database
 * QR Code scanner
