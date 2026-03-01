// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka)
}

dokka {
    moduleName.set("VerificationCodeReader")
}

// Aggregate Dokka documentation from library subprojects
dependencies {
    dokka(project(":sms-reader"))
    dokka(project(":otp-input-kit"))
}