# Android Library

The Feedback Mechanisms for Android are realized as an Android Library Module. The Android Library has the following dependencies (see the build.gradle file under 'android_library/feedbacklibrary'):
* junit:junit:4.12
* com.android.support:appcompat-v7:24.2.1
* com.android.support:design:24.2.1
* com.google.code.gson:gson:2.6.2
* com.squareup.retrofit2:retrofit:2.0.0
* com.squareup.retrofit2:converter-gson:2.0.0
* com.theartofdev.edmodo:android-image-cropper:2.3.1

## Setup Instructions

### 1. Build the feedback library:

1. Check out the android_library project in your desired destination folder.
2. Build the android library, i.e., the feedbacklibrary-release.aar file with either of those two options:
   * Command line (recommended):
     1. Step into the root folder of the checked out project, i.e., android_library.
     2. Eecute the following command *./gradlew build* to build all the resources (*./gradlew clean* in order to clean all the build files)
   * Android Studio:
     1. Import the checked out project into Android Studio.
     2. Go to *Build* --> *Rebuild Project*
3. The feedback library feedbacklibrary-release.aar file is in the following path /feedbacklibrary/build/outputs/aar
Regarding the Lint errors, check the following link/ticket:<br>
https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit#issues

### 2. Integration of the feedback library:

In this example Android Studio is used as the IDE with Gradle as the build tool. So the exact steps may differ in other IDEs, yet the logic of integrating the library is the same.

1. Choose where to launch a push or a pull feedback.
2. Choose the 'Android' view in Android Studio.
3. Right click on your application folder → *New* → *Module* → *Import .JAR/.AAR Package*
4. Choose the location of the feedbacklibrary-release.aar file and click finish.
5. Right click on the your application folder → *Open Module Settings* → *Dependencies Tab*
6. Add the feedbacklibrary-release.aar file as a module dependency and press OK.
7. Before you build your application, make sure the following prerequisites are met:
   * The build.gradle file of your application must exhibit the following dependencies:
     * com.google.code.gson:gson:2.6.2 (or higher)
     * com.squareup.retrofit2:retrofit:2.0.0 (or higher)
     * com.squareup.retrofit2:converter-gson:2.0.0 (or higher)
     * project(':feedbacklibrary-release') (from adding the library as a module)
   * The AndroidManifest.xml file of your application must exhibit the following:
     * tools:replace="android:label, android:allowBackup"
   * The minimum SDK must be 17 or higher.
8. In order to use the library, only one import statement is needed:
   import com.example.matthias.feedbacklibrary.utils.Utils;

More information about the usage of the library can be found in the GitHub Wiki in the Usage section under:
https://github.com/supersede-project/monitor_feedback/wiki/Usage#android-client
