import org.jetbrains.kotlin.cli.jvm.main

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.test.audiostreamer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.test.audiostreamer"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        externalNativeBuild {
            ndkBuild {
                var gstRoot : String

                if (project.hasProperty("gstAndroidRoot"))
                    gstRoot = project.property("gstAndroidRoot").toString()
                else
                    gstRoot = System.getenv("GSTREAMER_ROOT_ANDROID")

                if (gstRoot == null)
                    throw GradleException("GSTREAMER_ROOT_ANDROID must be set, or \"gstAndroidRoot\" must be defined in your gradle.properties in the top level directory of the unpacked universal GStreamer Android binaries")

                arguments("NDK_APPLICATION_MK=jni/Application.mk", "GSTREAMER_JAVA_SRC_DIR=src/main/java", "GSTREAMER_ROOT_ANDROID=$gstRoot", "GSTREAMER_ASSETS_DIR=src/main/java/assets")

                targets("tutorial-2")

                abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            }
        }
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

    externalNativeBuild {
        ndkBuild {
            path("jni/Android.mk")
        }
    }

    ndkVersion = "25.2.9519653"
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}