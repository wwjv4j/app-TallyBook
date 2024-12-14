

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tallybook"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tallybook"
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
    implementation("androidx.recyclerview:recyclerview:1.2.1") // 添加RecyclerView依赖
    implementation("androidx.room:room-runtime:2.4.2") // 添加Room依赖
    annotationProcessor("androidx.room:room-compiler:2.4.2") // 添加Room编译器依赖
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}