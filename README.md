# ColonelFund
Fundraising App

[![Travis CI build status](https://travis-ci.org/cwpenner/Colonel-Fund.svg?branch=master)](https://travis-ci.org/cwpenner/Colonel-Fund)

## App Notes
For accounts that have not been set up in the database, ie. non-ColonelFund accounts, the userID will default to that of the "admin" account, and all events created under that account will appear associated with admin. As a result, while logged in with a non-ColonelFund account, all admin events will appear as your events. This was a workaround until account linking was implemented. Account linking has not been implemented, so the Link Your Account button has no functionality at this time.

A payment server has not been implemented, and as such, the payment functionality will not result in any transactions taking place. Because of this, the currently raised funds for an event will not be updated after a donation. As well, the Transaction ID will be blank on the Transaction Summary screen.

## Android Notes
In order to access the ability to use Google Pay or Google Login in testing, you must run the app using an emulator that uses the Google Play services, specifically the `Nexus 5X` or `Nexus 5` using `Android API 27 (Google Play)`, instead of `Pixel 2 XL` using `Android API 27 (Google APIs)` (also note the Google Play icon in the Play Store column when creating the new virtual machine).

If you are not specifically testing the Google Pay functionality, the rest of the app works using the virtual machine of your choice (ie. Pixel 2 XL).


## iOS Notes
To open this Xcode project, you need to load the `ColonelFund.xcworkspace` file in Xcode, and NOT the default `ColonelFund.xcodeproj` file.

The reason for this is that many third-party SDKs do not officially support the Swift programming language, requiring CocoaPods to link all the already-made Objective-C SDKs properly with corresponding Swift frameworks.

If the `.xcodeproj` file is loaded instead of the `.xcworkspace file`, nothing bad will happen, but the app will not compile. Simply close the project, and reopen it using the `.xcworkspace` file.


### Login Notes
username: `admin@colonelfund.com`
password: `admin`

Logging in with Facebook works for any of the Facebook test accounts that have been created so far, or if your Facebook ID has been added as an admin.

Logging in with Google requires your specific SHA-1 key to be added in the Google admin portal.