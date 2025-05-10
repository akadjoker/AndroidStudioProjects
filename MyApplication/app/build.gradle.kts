plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.djokersoft.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.djokersoft.myapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
 
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

}

dependencies {






}