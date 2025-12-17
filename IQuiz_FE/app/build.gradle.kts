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

    // ANDROIDX & UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // BỔ SUNG: CÁC THƯ VIỆN CÒN THIẾU CHO GIAO DIỆN ONLINE
    // 1. Thư viện RecyclerView (BẮT BUỘC)
    // Cần cho: FriendsAdapter, LeaderboardAdapter, RoomPlayerAdapter, ResultAdapter
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // 2. Thư viện Fragment (BẮT BUỘC)
    // Cần cho: FriendsFragment, LeaderboardFragment
    implementation("androidx.fragment:fragment-ktx:1.6.2") // Dùng bản ktx

    // 3. Thư viện ViewPager2 (BẮT BUỘC)
    // Cần cho: FriendsLeaderboardActivity (để lướt qua lại giữa các Fragment)
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    // KẾT THÚC BỔ SUNG

    // ROOM (DATABASE CỤC BỘ)
    implementation(libs.room.common.jvm)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler) // Cần annotationProcessor để Room tạo code

    // RETROFIT & OKHTTP (GIAO TIẾP MẠNG) - ĐÃ SỬA CÚ PHÁP KTS

    // 1. Retrofit Core
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // 2. Gson Converter (Chuyển đổi JSON)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // 3. OkHttp (Client HTTP)
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // 4. Logging Interceptor (Theo dõi request/response trong Logcat)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Thư viện này bạn đã thêm, rất tốt!
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // TESTING
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation ("com.github.bumptech.glide:glide:4.15.1")

}