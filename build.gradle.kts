// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id ("com.google.dagger.hilt.android") version "2.49" apply false // Hilt plugin

}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.1.0") // Use latest stable version
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.47") // Hilt for DI
    }
}