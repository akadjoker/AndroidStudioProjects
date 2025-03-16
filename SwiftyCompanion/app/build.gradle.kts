import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "com.djokersoft.swiftycompanion"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.djokersoft.swiftycompanion"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "CLIENT_ID", "\"${localProperties.getProperty("client.id", "")}\"")
            buildConfigField("String", "CLIENT_SECRET", "\"${localProperties.getProperty("client.secret", "")}\"")

        }
        debug {
            buildConfigField("String", "CLIENT_ID", "\"${localProperties.getProperty("client.id", "")}\"")
            buildConfigField("String", "CLIENT_SECRET", "\"${localProperties.getProperty("client.secret", "")}\"")

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)


    // OkHttp para requisições HTTP
    implementation(libs.okhttp)

    //    JSON
    implementation(libs.gson)

    //  carregamento de imagens
    implementation(libs.glide)
    implementation (libs.androidsvg)

    //   imagens de perfil circulares
    implementation(libs.circleimageview)




    implementation(libs.viewpager2)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}