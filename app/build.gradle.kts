plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.smartspend2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.smartspend2"
        minSdk = 26  // Lowered for broader device support
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
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.kotlin.stdlib)

    // Core Android Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // Gson for JSON parsing
    implementation(libs.gson)

    // MPAndroidChart for PieChart
    implementation(libs.mpandroidchart)

    // Coroutines for background tasks (if needed)
    implementation(libs.kotlinx.coroutines.android)


    // Lifecycle and Coroutine Support (Modern Approach)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // SQLite Database
    implementation("androidx.sqlite:sqlite:2.3.1")
    implementation("androidx.sqlite:sqlite-framework:2.3.1")
    implementation(libs.androidx.room.runtime.android)

    // Testing Libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
