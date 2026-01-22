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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
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
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    /* -------- Jetpack Compose (BOM) -------- */
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
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
    implementation("com.google.ai.client.generativeai:generativeai:0.2.0")

    /* -------- Image Loading -------- */
    implementation("io.coil-kt:coil-compose:2.6.0")

    /* -------- Persistence -------- */
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.code.gson:gson:2.10.1")

    /* -------- Coroutines -------- */
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    /* -------- Monetization -------- */
    implementation("com.google.android.gms:play-services-ads:23.0.0")
    implementation("com.android.billingclient:billing:6.2.0")

    /* -------- Debug Tools -------- */
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
