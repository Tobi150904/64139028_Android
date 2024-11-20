plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "vn.ngoviethoang.cuoiki"
    compileSdk = 34

    defaultConfig {
        applicationId = "vn.ngoviethoang.cuoiki"
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
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.activity:activity:1.7.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Room Database
    implementation("androidx.room:room-runtime:2.5.0")
    annotationProcessor("androidx.room:room-compiler:2.5.0") // Sử dụng annotation processor cho Room

    // MPAndroidChart để vẽ biểu đồ
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Glide để load ảnh
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // RecyclerView để quản lý danh sách
    implementation("androidx.recyclerview:recyclerview:1.3.1")
}
