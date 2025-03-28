plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.travel_master_yyz"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.travel_master_yyz"
        minSdk = 24
        targetSdk = 33
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
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("io.github.youth5201314:banner:2.2.2")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("com.airbnb.android:lottie:5.2.0")
    // Glide 核心库
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // PictureSelector 基础 (必须)
    implementation ("io.github.lucksiege:pictureselector:v3.11.2")

    // 图片压缩 (按需引入)
    implementation ("io.github.lucksiege:compress:v3.11.2")

    implementation ("com.google.android.material:material:1.6.0") // Material Design组件
}