plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "vn.ngoviethoang.duancuoiky"
    compileSdk = 34

    defaultConfig {
        applicationId = "vn.ngoviethoang.duancuoiky"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.common)  // Room Database

    // MPAndroidChart
    implementation(libs.mpandroidchart)

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.room.runtime)
    implementation(libs.play.services.base)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")  // Annotation Processor cho Glide

    implementation(libs.play.services.auth)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)
}