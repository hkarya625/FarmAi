plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
//    alias(libs.plugins.ksp)
    kotlin("kapt")
}

android {
    namespace = "com.himanshu_arya.voice_input"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "11"
    }


}

dependencies {

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.material3.android) // Use latest version
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // For ViewModel injection in Compose

    // Google Cloud Speech
    implementation("com.google.cloud:google-cloud-speech:4.33.0") { // Use latest version
        exclude(group = "org.apache.httpcomponents", module = "httpclient") // Example exclusion if conflicts arise
    }
    implementation("io.grpc:grpc-okhttp:1.63.0") // Required by Speech client, use compatible/latest version

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1") // For GoogleCredentials potentially


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
kapt {
    correctErrorTypes = true
}