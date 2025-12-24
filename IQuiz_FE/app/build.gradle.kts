plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.iq5"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.iq5"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Needed to access BuildConfig fields (BASE_URL)
    buildFeatures {
        buildConfig = true
    }

    // Two build variants so you don't have to edit BASE_URL by hand:
    // - phoneDebug  : real device + ADB reverse -> http://127.0.0.1:5048/
    // - emulatorDebug: Android Emulator -> http://10.0.2.2:5048/
    flavorDimensions += "env"
    productFlavors {
        create("phone") {
            dimension = "env"
            buildConfigField("String", "BASE_URL", "\"http://127.0.0.1:5048/api/\"")
        }
        create("emulator") {
            dimension = "env"
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:5048/api/\"")
        }
        // Optional: if you truly want Wi‑Fi IP based testing
        // create("wifi") {
        //     dimension = "env"
        //     buildConfigField("String", "BASE_URL", "\"http://192.168.1.6:5048/api/\"")
        // }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // Keep debug simple; network_security_config is already in manifest
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // ANDROIDX & UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // RecyclerView / Fragment / ViewPager2
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // ROOM (Local DB) - Java project: use annotationProcessor
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // RETROFIT & OKHTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Images
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // Gson (only keep ONE)
    implementation("com.google.code.gson:gson:2.10.1")

    // TESTING
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
