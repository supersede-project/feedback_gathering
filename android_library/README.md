# Android Library

The Feedback Mechanisms for Android are realized as an Android Library Module. It has the following dependencies:

## Setup

Creating and using the APK and AAR (example using Android Studio):

1. Check out the android_library project
2. Make project (Build -> Make Project)
3. Run the application on an emulator or actual device (Run -> Run App)
  * android_library/app/build/outputs/apk contains the apk (app-debug.apk) which is just a small sample application already including the feedback library.
  * android_library/feedbacklibrary/build/outputs/aar contains the apk (feedbacklibrary-release.aar)

## Deployment

For simply trying out the feedback library:
- Install the APK, copy the APK to your device, locate it and execute it (permission for untrusted sources must be granted, because the APK is not signed).

*or*

For developers to integrate the feedback library into their own application:
- Including the AAR into an existing Android Project:
  1. Go to File -> Project Structure -> New Module -> Import .JAR/.AAR Package, choose the aar file to be imported and click finish.
  2. Add the following dependencies to the gradle file of your main application:
    * compile 'com.google.code.gson:gson:2.6.2'
    * compile 'com.squareup.retrofit2:retrofit:2.0.0'
    * compile 'com.squareup.retrofit2:converter-gson:2.0.0'
    * compile project(':nameOfTheAARFile')
  4. In your AndroidManifest add the following attribute to the application:
    * tools:replace="android:label"
  5. In the class you want to start the new feedback activity add the following lines:
    * import com.example.package.nameOfTheAARFile.FeedbackActivity;
    * Intent intent = new Intent(this, FeedbackActivity.class); and startActivity(intent);
