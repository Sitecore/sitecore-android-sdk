Change Log
==========

## Version 2.0 *(xx-yyy-2014)*

### Sitecore SDK
 * Gradle wrapper updated to v1.11
 * Android Support package updated to v.19.0.1
 * Sitecore SDK separated into four modules:
	- sitecore-api-android
	- sitecore-js-android
	- sitecore-ui-android
	- sitecore-qr-reader
 
### Javascript API
 * Added ability to upload media items
 
### Sitecore UI Components:
 * ItemsBroserFragment separated into:
	- ItemsListBrowserFragment
	- ItemsGridBrowserFragment
 
### Items Web Api
 * Various API naming changes to be more consistent with Sitecore Mobile SDK for iOs
 * Added ability to specify media library path
 * Upload Media APIs updated to be more consistent with Items CRUD APIs
 * Added ability to specify default session parameters:
	- site
	- database
	- language
 * Added handy methods:
	- Load children of specific item
	- Edit fields of specific item
	- Delete specific item

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
