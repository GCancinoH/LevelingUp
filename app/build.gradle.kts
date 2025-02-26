plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.gcancino.levelingup"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gcancino.levelingup"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.worker)

    // Material
    //implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.alpha)
    implementation(libs.androidx.material.icons)
    implementation(libs.androidx.material.icons.extended)

    // Google
    implementation(libs.google.barcode.scanner)
    implementation(libs.google.auth)
    implementation(libs.google.gson)
    implementation(libs.google.permissions)

    // Firebase
    implementation(platform(libs.google.firebaseBom))
    implementation(libs.google.firebase.auth)
    implementation(libs.google.firebase.firestore)
    implementation(libs.google.firebase.storage)
    implementation(libs.google.firebase.messaging)

    // Coroutines
    implementation(libs.kotlin.coroutines)
    // Navigation
    implementation(libs.navigation.compose)
    // SplashScreen
    implementation(libs.splashScreen)
    // DataStore
    implementation(libs.storage.datastore)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)
    annotationProcessor(libs.room.processor)

    // Camera
    implementation(libs.camerax.core)
    //implementation(libs.camerax.compose)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camera.view)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.coil.image)
    implementation("com.onesignal:OneSignal:[5.0.0, 5.99.99]")
    implementation(libs.glance.appwidget)
    implementation(libs.glance.appwidget.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}