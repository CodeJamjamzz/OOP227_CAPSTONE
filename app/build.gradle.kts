plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.capstone_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.capstone_project"
        minSdk = 26
        targetSdk = 34
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.gson)
    implementation(libs.zxing.core)
    implementation(libs.zxing.android.embedded)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.cardview)
    implementation(libs.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    implementation(libs.barcode.scanning)
    implementation(libs.play.services.tasks)
    implementation(libs.androidx.core.ktx)
}