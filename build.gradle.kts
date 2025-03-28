// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.6.0" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.0" apply false
}