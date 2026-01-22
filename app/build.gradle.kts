plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.itpdf.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.itpdf.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // রিলিজের সময় অ্যাপের সাইজ ছোট করার জন্য এটি true রাখা ভালো
            // তবে মনে রাখবেন, Gson ব্যবহারের কারণে proguard-rules.pro ফাইলে রুলস যোগ করতে হবে
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    // SDK 34 এর জন্য Java 17 ব্যবহার করা বর্তমান স্ট্যান্ডার্ড
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // Kotlin ভার্সনের সাথে কম্পোজ কম্পাইলারের মিল থাকতে হয়
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    /* -------- Core Android -------- */
    implementation("androidx.core:core-ktx:1.13.1") // আপডেটেড
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0") // আপডেটেড
    implementation("androidx.activity:activity-compose:1.9.0") // আপডেটেড

    /* -------- XML Theme Fix (CRITICAL) -------- */
    // আপনার আগের এররটি ঠিক করার জন্য এই লাইনটি সবচেয়ে জরুরি
    implementation("com.google.android.material:material:1.12.0")

    /* -------- Jetpack Compose (BOM) -------- */
    // ২০২৩ এর ভার্সন অনেক পুরনো, এটি ২০২৪ এ আপডেট করা হলো
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    /* -------- Material 3 -------- */
    implementation("androidx.compose.material3:material3")

    /* -------- Icons -------- */
    implementation("androidx.compose.material:material-icons-extended")

    /* -------- Navigation -------- */
    implementation("androidx.navigation:navigation-compose:2.7.7")

    /* -------- Gemini AI -------- */
    // ০.২.০ অনেক পুরনো এবং এতে অনেক বাগ ছিল। ০.৯.০+ স্টেবল।
    implementation("com.google.ai.client.generativeai:generativeai:0.9.1")

    /* -------- Image Loading -------- */
    implementation("io.coil-kt:coil-compose:2.6.0")

    /* -------- Persistence -------- */
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.google.code.gson:gson:2.10.1")

    /* -------- Coroutines -------- */
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    /* -------- Monetization -------- */
    implementation("com.google.android.gms:play-services-ads:23.1.0")
    implementation("com.android.billingclient:billing:7.0.0")

    /* -------- Debug Tools -------- */
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
